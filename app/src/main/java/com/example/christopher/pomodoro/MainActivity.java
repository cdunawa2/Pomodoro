package com.example.christopher.pomodoro;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

class Mp3Filter implements FilenameFilter{

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".mp3");
    }
}

public class MainActivity extends ListActivity {


    private static final int WRITE_STORAGE_PERMISSIONS_REQUEST = 1;
    private static final int READ_STORAGE_PERMISSIONS_REQUEST = 1;
    private static final String SD_PATH = new String("/sdcard/");
    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private List<String> songs = new ArrayList<String>();
    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updatePlayist();

        //Check for READ/WRITE Permissions
        int permissionCheck_write_external = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck_write_external != PackageManager.PERMISSION_GRANTED) {

            // request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        int permissionCheck_read_external = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck_read_external != PackageManager.PERMISSION_GRANTED) {

            // request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        Button stopPlay = (Button) findViewById(R.id.stopBtn);
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
            }
        });
    }

    private void updatePlayist() {
//        File home = new File(SD_PATH);
//        File home = Environment.getExternalStorageDirectory();
        File home = new File(MEDIA_PATH);
//        File [] fileList= home.listFiles(new Mp3Filter());
        File [] fileList= home.listFiles();
        if(!isExternalStorageReadable()){
            Log.d("External Storage:", "updatePlaylist: Storage Unreadable");
        }
        else if(fileList != null && fileList.length > 0){
            for(File file : home.listFiles(new Mp3Filter())){
                songs.add(file.getName());
            }

            ArrayAdapter<String> songlist = new ArrayAdapter<String>(this, R.layout.song_item,songs);
            setListAdapter(songlist);
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
