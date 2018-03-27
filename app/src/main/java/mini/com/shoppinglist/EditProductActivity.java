package mini.com.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProductActivity extends AppCompatActivity {
    private EditText mEditProductEditText;
    private Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Intent intent = getIntent();
        final Product productToReturn = (Product)intent.getSerializableExtra("product");
        final int position = intent.getIntExtra("position", 0);

        mEditProductEditText = findViewById(R.id.new_product_name);
        mEditProductEditText.setHint("New product name");
        mEditProductEditText.setText(productToReturn.Name);

        mEditButton = findViewById(R.id.add_button);
        mEditButton.setText("Edit product");

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int resultCode = RESULT_OK;
                productToReturn.Name = mEditProductEditText.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("edited_product", productToReturn);
                resultIntent.putExtra("position", position);
                setResult(resultCode, resultIntent);
                Log.d("cojestkurwa2", Integer.toString(resultCode));
                finish();
            }
        });
    }
}
