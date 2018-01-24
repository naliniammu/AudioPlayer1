package com.squareandcube.audioplayer1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    Button btnOpenDocument, btnGetContent, btnPick;
    TextView info;
    VideoView myVideoView;
    MediaPlayer mmediaPlayer;

    Uri audioFileUri = null;

    final static int RQS_OPEN_DOCUMENT = 4;
    final static int RQS_GET_CONTENT = 2;
    final static int RQS_PICK = 3;

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVideoView = (VideoView)findViewById(R.id.vview);
        btnOpenDocument = (Button)findViewById(R.id.opendocument);
        btnGetContent = (Button)findViewById(R.id.getcontent);
        btnPick = (Button)findViewById(R.id.pick);
        btnOpenDocument.setOnClickListener(btnOpenDocumentOnClickListener);
        btnGetContent.setOnClickListener(btnGetContentOnClickListener);
        btnPick.setOnClickListener(btnPickOnClickListener);

        //mmediaPlayer = MediaPlayer.create(this);
        mediaController = new MediaController(MainActivity.this);
        myVideoView.setMediaController(mediaController);
    }

    private void prepareAudio(){

        Toast.makeText(MainActivity.this,
                audioFileUri.toString(),
                Toast.LENGTH_LONG).show();
        myVideoView.setVideoURI(audioFileUri);

        myVideoView.setOnCompletionListener(myVideoViewCompletionListener);
        myVideoView.setOnPreparedListener(MyVideoViewPreparedListener);
        myVideoView.setOnErrorListener(myVideoViewErrorListener);

        myVideoView.requestFocus();
        myVideoView.start();

    }

    MediaPlayer.OnCompletionListener myVideoViewCompletionListener =
            new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    Toast.makeText(MainActivity.this, "End of Video",
                            Toast.LENGTH_LONG).show();
                }
            };

    MediaPlayer.OnPreparedListener MyVideoViewPreparedListener =
            new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    long duration = myVideoView.getDuration(); //in millisecond
                    Toast.makeText(MainActivity.this,
                            "Duration: " + duration + " (ms)",
                            Toast.LENGTH_LONG).show();

                }
            };

    MediaPlayer.OnErrorListener myVideoViewErrorListener =
            new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    String errWhat = "";
                    switch (what){
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            errWhat = "MEDIA_ERROR_UNKNOWN";
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            errWhat = "MEDIA_ERROR_SERVER_DIED";
                            break;
                        default: errWhat = "unknown what";
                    }

                    String errExtra = "";
                    switch (extra){
                        case MediaPlayer.MEDIA_ERROR_IO:
                            errExtra = "MEDIA_ERROR_IO";
                            break;
                        case MediaPlayer.MEDIA_ERROR_MALFORMED:
                            errExtra = "MEDIA_ERROR_MALFORMED";
                            break;
                        case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                            errExtra = "MEDIA_ERROR_UNSUPPORTED";
                            break;
                        case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                            errExtra = "MEDIA_ERROR_TIMED_OUT";
                            break;
                        default:
                            errExtra = "...others";

                    }

                    Toast.makeText(MainActivity.this,
                            "Error!!!\n" +
                                    "what: " + errWhat + "\n" +
                                    "extra: " + errExtra,
                            Toast.LENGTH_LONG).show();
                    return true;
                }
            };

    View.OnClickListener btnOpenDocumentOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "ACTION_OPEN_DOCUMENT"), RQS_OPEN_DOCUMENT);


        }
    };

    View.OnClickListener btnGetContentOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser( intent, "ACTION_GET_CONTENT"), RQS_GET_CONTENT);
        }
    };

    View.OnClickListener btnPickOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent, "ACTION_PICK"), RQS_PICK);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == RQS_OPEN_DOCUMENT
                    || requestCode == RQS_GET_CONTENT
                    || requestCode == RQS_PICK){

                audioFileUri = data.getData();
                // info.setText(videoFileUri.toString());
                prepareAudio();
            }
        }
    }



    }

