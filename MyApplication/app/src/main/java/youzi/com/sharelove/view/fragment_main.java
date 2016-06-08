package youzi.com.sharelove.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.mobiwise.playerview.MusicPlayerView;
import youzi.com.sharelove.R;

/**
 * Created by youzi 2016/5/28.
 */
@SuppressLint("ValidFragment")
public class fragment_main extends Fragment {

    String content;
    MusicPlayerView s;

    public fragment_main(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        s = (MusicPlayerView) rootView.findViewById(R.id.play);
        s.setCoverDrawable(R.drawable.playss);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.isRotating()) {
                    s.stop();
                } else {
                    s.start();
                }
            }
        });
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(content);
        return rootView;
    }
}

