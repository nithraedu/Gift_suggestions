package nithra.tamil.word.game.giftsuggestions.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import nithra.tamil.word.game.giftsuggestions.ActivitySecond;
import nithra.tamil.word.game.giftsuggestions.MyProduct;
import nithra.tamil.word.game.giftsuggestions.R;

public class Product extends Fragment {
    EditText productname, prod_prize, offer_prize, offer_percentage, prod_des;
    Button save,BSelectImage;
    Spinner  spin_occaction, spin_gender;
    TextView myproduct;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;

    public Product() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        productname = view.findViewById(R.id.productname);
        spin_occaction = view.findViewById(R.id.spin_occaction);
        spin_gender = view.findViewById(R.id.spin_gender);
        BSelectImage=view.findViewById(R.id.BSelectImage);
        prod_prize=view.findViewById(R.id.prod_prize);
        offer_prize=view.findViewById(R.id.offer_prize);
        offer_percentage=view.findViewById(R.id.offer_percentage);
        prod_des=view.findViewById(R.id.prod_des);
        save=view.findViewById(R.id.save);
        myproduct=view.findViewById(R.id.myproduct);
        IVPreviewImage = view.findViewById(R.id.IVPreviewImage);
        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();

            }
        });
        myproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), MyProduct.class);
                startActivity(intent);
            }
        });
        return view;
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}