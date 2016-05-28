package youzi.com.sharelove.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import youzi.com.sharelove.R;

/**
 * Created by youzi 2016/5/28.
 */
public class fragment_listen extends Fragment {

    String content;//页面传递的内容

    public fragment_listen(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_listen, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(content);
        return rootView;
    }
}
