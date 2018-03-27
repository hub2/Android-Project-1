package mini.com.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddProductActivity extends AppCompatActivity {
    private EditText mNewProductNameEditText;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mNewProductNameEditText = findViewById(R.id.new_product_name);
        mAddButton = findViewById(R.id.add_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int resultCode = RESULT_OK;
                Product productToReturn = new Product(mNewProductNameEditText.getText().toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_product", productToReturn);
                setResult(resultCode, resultIntent);
                finish();
            }
        });
    }
}
