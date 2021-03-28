package tyrael.duobao.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import in.tyrael.raider.databinding.ActivityDeveloperBinding;
import tyrael.duobao.jdapi.DuobaoApi;

public class DeveloperActivity extends AppCompatActivity {
    private ActivityDeveloperBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityDeveloperBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.requestReminderListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DuobaoApi.getDuobaoApi().reminderList();
                    }
                }).start();

            }
        });
    }
}