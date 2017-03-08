package mbp.alexpon.com.hkbike;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PictureShare extends Activity {

    ImageView imageView;
    Button button;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_share);
        Intent in = getIntent();
        if(in != null){
            byte[] bis = in.getByteArrayExtra("image");
            System.out.println("-------CameraDemo bis length=" + bis.length);
            bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            imageView=(ImageView)findViewById(R.id.imageView3);
            imageView.setImageBitmap(bitmap);}
        button=(Button)findViewById(R.id.button2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(bitmap);
            }
        });

    }


    private void shareImage(Bitmap bitmap) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");

        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));

        startActivity(Intent.createChooser(share, "Share Image!"));
    }
}
