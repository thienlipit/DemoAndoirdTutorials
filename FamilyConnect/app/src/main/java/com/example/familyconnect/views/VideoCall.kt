package com.example.familyconnect.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.familyconnect.R
import com.example.familyconnect.databinding.ActivityVideoCallBinding
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

class VideoCall : AppCompatActivity() {
    lateinit var binding: ActivityVideoCallBinding
    private var mMuted = false
    private var localFrame: SurfaceView? = null
    private var remoteFrame: SurfaceView? = null

    private var mRtcEngine: RtcEngine?= null

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote user joining the channel to get the uid of the user.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                setupRemoteVideo(uid)
            }
        }
    }

    private val PERMISSION_REQ_ID_RECORD_AUDIO = 22
    private val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(permission),
                requestCode)
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(
                Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            initializeAndJoinChannel()
        }

        binding.buttonEndCall.setOnClickListener {
            endCall()
        }

        binding.buttonSwitchCamera.setOnClickListener {
            mRtcEngine?.switchCamera()
        }

        binding.buttonMute.setOnClickListener {
            mMuted = !mMuted
            mRtcEngine?.muteLocalAudioStream(mMuted)
            val res: Int = if (mMuted) {
                R.drawable.btn_mute
            } else {
                R.drawable.btn_unmute
            }

            binding.buttonMute.setImageResource(res)
        }
    }

    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.APP_ID), mRtcEventHandler)
        } catch (e: Exception) {

        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine!!.enableVideo()

        val localContainer = binding.localVideoViewContainer
        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        localFrame = RtcEngine.CreateRendererView(baseContext)
        localFrame!!.setZOrderMediaOverlay(true)
        localContainer.addView(localFrame)
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(localFrame, VideoCanvas.RENDER_MODE_FIT, 0))

        // Join the channel with a token.
        mRtcEngine!!.joinChannel(getString(R.string.TOKEN), getString(R.string.CHANNEL), "", 0)
    }

    // Kotlin
    private fun setupRemoteVideo(uid: Int) {
        val remoteContainer = binding.remoteVideoViewContainer

        remoteFrame = RtcEngine.CreateRendererView(baseContext)
        remoteContainer.addView(remoteFrame)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteFrame, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    private fun endCall() {
        removeLocalVideo()
        removeRemoteVideo()
        leaveChannel()
    }
    private fun removeLocalVideo() {
        if (localFrame != null) {
            binding.localVideoViewContainer.removeView(localFrame)
        }
        localFrame = null
    }

    private fun removeRemoteVideo() {
        if (remoteFrame != null) {
            binding.remoteVideoViewContainer.removeView(remoteFrame)
        }
        remoteFrame = null
    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }


    override fun onDestroy() {
        super.onDestroy()

        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
    }
}