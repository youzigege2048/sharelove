package youzi.com.sharelove.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import youzi.com.sharelove.R;

/**
 * Created by youzi 2016/5/28.
 */
public class fragment_about extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listen, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("来自一份爱意的歌单");
        return rootView;
    }
}
