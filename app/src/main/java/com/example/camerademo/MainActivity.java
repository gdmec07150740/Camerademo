package com.example.camerademo;


import android.graphics.Camera;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private ImageView iv;
    private File file;
    private android.hardware.Camera camera;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView msv= (SurfaceView) this.findViewById(R.id.sv);
        SurfaceHolder msh=msv.getHolder();
        msh.addCallback(this);
        msh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public void takephoto(View v){
        camera.takePicture(null,null,pictureCallback);

    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera= android.hardware.Camera.open();
        android.hardware.Camera.Parameters params=camera.getParameters();
        params.setFlashMode(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            camera.release();
            camera=null;
        }
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    android.hardware.Camera.PictureCallback pictureCallback=new android.hardware.Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {
            if (bytes!=null){
                try {
                    savePicture(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    };
    public void savePicture(byte[] bytes) throws IOException {
        String imageId=System.currentTimeMillis()+"";
        String path=android.os.Environment.getExternalStorageDirectory().getPath()+"/";
        File file=new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        path+=imageId+",jpg";
        file=new File(path);
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        Toast.makeText(this,"path:"+path,Toast.LENGTH_LONG).show();

    }
}

