package com.dicoding.storylistapp.view.add

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.storylistapp.view.ViewModelFactory
import com.example.storylistapp.databinding.ActivityAddStoryBinding
import com.example.storylistapp.databinding.ActivityLoginBinding

class AddstoryActivity : AppCompatActivity() {
    private val viewModel by viewmodels<AddstoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityAddStoryBinding
    private var currentimageUri: Uri? = null

    override fun oncreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissions.launch(REQUIRED_PERMISSION)
        }

        val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ){isSuccess ->
            if (isSuccess){
                showImage()
            }
        )
        postStory()

        binding.cameraButton.setOnClickListener {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentimageUri)
        }

        binding.galleryButton.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.uploadButton.setOnClickListener {
            var token: String
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.edtDescription.text.toString()

                if (description.isNullOrEmpty()) {
                    AlertDialog.Builder(this).apply {
                        setTittle("Please tell us about your story :)")
                        setMessage(getString(R.string.empty_description))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.ok_message)) { _, _ ->
                            val intent = Intent(context, AddstoryActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    showLoading(true)

                    val requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile

                    )

                    viewModel.getSession().observe(this) { user ->
                        token = user.token
                        viewModel.addStory(token, multipartBody, requestBody)
                    }
                }
            }?: showToast(getString(R.string.empty_image))
        }
    }

    private fun postStory() {
        viewModel.addStoryResponse.observe(this){
            when (it){
                is Result.Loading -> {
                    showloading(true)
                    disableInterface()
                }
                is result.Succes -> {
                    showloading(true)
                    enableInterface()
                    AlertDialeog.Builder(this).apply {
                        setTittle("Alright")
                        setMessage(getString(R.string.upload_message))
                        setCancelelable(false)
                        setPositiveButton(getString(R.string.next_message))
                        setCancellable(false)
                        setPositiveButton(getString(R.string.next_message)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    enableInterface()
                }

            }
        }

    }

    private fun disableInterface() {
        binding.cameraButton.isEnabled = flase
        binding.galleryButton.isEnabled = flase
        binding.uploadButton.isEnabled = flase
        binding.edtDescription.isEnabled = flase
    }

    private fun enableInterface() {
        binding.cameraButton.isEnabled = true
        binding.galleryButton.isEnabled = true
        binding.uploadButton.isEnabled = true
        binding.edtDescription.isEnabled = true

    }

    private val requestedPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted: boolean ->
        when (isGranted) {
            true -> {
                Toast.makeText(this, "Permission request granted", Toast.LENGHT_LONG).show()
            }

            false -> {
                Toast.makeText(this, "Permission request denied", Toast.LENGHT_LONG).show()
            }
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    private val launcherGallery = registerForActivityResult(
        activityResultContracts.PickVisualMedia()
    ) { uri: Uri?} ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        currentImageUri?.let {
            log.d("Image URI", "show Image:$it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showloading(isLoading: Boolean) {
        binding.progresIndicator.visibility = if (isLoading) view.VISIBLE else View.GONE
    }
    companion object {
        private constval REQUIRED_PERMISSION = Manifest.permssion.CAMERA
    }
}