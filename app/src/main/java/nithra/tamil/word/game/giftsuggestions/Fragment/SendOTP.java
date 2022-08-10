package nithra.tamil.word.game.giftsuggestions.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.R;


public class SendOTP extends Fragment {

    FragMove fragMove;
    TextView getotp;

    public SendOTP() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_o_t_p, container, false);
        fragMove = (FragMove) getContext();
        getotp = view.findViewById(R.id.getotp);
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragMove.enterotp();
            }
        });
        return view;
    }
}