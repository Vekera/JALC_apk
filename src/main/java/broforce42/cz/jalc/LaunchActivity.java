package broforce42.cz.jalc;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.IOException;

public class LaunchActivity extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        startAnimations();
    }
    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        //setupPDF();


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(50);
                        waited += 50;
                    }
                    Intent intent = new Intent(LaunchActivity.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    LaunchActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    LaunchActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }

    private void setupPDF(){
        PDFBoxResourceLoader.init(getApplicationContext());
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream;
        PDFont font = PDType1Font.TIMES_ROMAN;
        try {
            contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, 25);
            contentStream.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
