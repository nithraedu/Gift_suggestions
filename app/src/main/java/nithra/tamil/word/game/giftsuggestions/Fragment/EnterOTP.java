package nithra.tamil.word.game.giftsuggestions.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nithra.tamil.word.game.giftsuggestions.FragMove;
import nithra.tamil.word.game.giftsuggestions.R;

public class EnterOTP extends Fragment {

    FragMove fragMove;
    TextView enterotp;
    public EnterOTP() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_enter_o_t_p, container, false);
        fragMove = (FragMove) getContext();
        enterotp = view.findViewById(R.id.enterotp);
        enterotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragMove.seller();
            }
        });
        return view;
    }
}