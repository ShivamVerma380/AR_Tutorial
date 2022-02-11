package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment arCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arCam = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arCam.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Toast.makeText(getApplicationContext(),"AR Cam clicked",Toast.LENGTH_SHORT).show();
            Anchor anchor = hitResult.createAnchor();
            ModelRenderable.builder()
                    .setSource(this, R.raw.wolf)
                    .setIsFilamentGltf(true)
                    .build()
                    .thenAccept(modelRenderable ->{
                        AnchorNode anchornode = new AnchorNode(anchor);
                        anchornode.setParent(arCam.getArSceneView().getScene());
                        TransformableNode transformableNode = new TransformableNode(arCam.getTransformationSystem());
                        transformableNode.setParent(anchornode);
                        transformableNode.setRenderable(modelRenderable);
                        transformableNode.select();
                            })
                    .exceptionally(throwable -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Something is not right" + throwable.getMessage()).show();
                        return null;
                    });
        });
    }
}