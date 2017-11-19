package bo.com.syscode.androidcampfirebaase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

	private EditText editEmail;
	private EditText editPassword;
	private Button btnSignin;
	private Button btnCreate;

	private Context context;
	private FirebaseAuth firebaseAuth;
	private FirebaseAuth.AuthStateListener authStateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;

		editEmail = (EditText) findViewById(R.id.edit_email);
		editPassword = (EditText) findViewById(R.id.edit_password);
		btnSignin = (Button) findViewById(R.id.btn_signin);
		btnCreate = (Button) findViewById(R.id.btn_create);

		btnSignin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					Toast.makeText(context, "Buton Iniciar Sesión", Toast.LENGTH_SHORT).show();
					//int div = 5/0;
					String email = editEmail.getText().toString();
					String password = editPassword.getText().toString();
					setUpSignin(email, password);
				} catch (Exception e) {
					FirebaseCrash.report(e);
					FirebaseCrash.logcat(Log.ERROR, e.getMessage(), "Error");
					finish();
				}
			}
		});

		btnCreate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(context, "Buton crear", Toast.LENGTH_SHORT).show();
				String email = editEmail.getText().toString();
				String password = editPassword.getText().toString();
				setUpCreate(email, password);
			}
		});

		setUpAuth();
	}

	private void setUpAuth() {
		firebaseAuth = FirebaseAuth.getInstance();
		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
				if (firebaseUser != null) {
					Toast.makeText(context, "Usuario:" + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "El usuario no esta autenticado", Toast.LENGTH_LONG).show();
				}
			}
		};
	}

	private void setUpSignin(String email, String password) {
		firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					Toast.makeText(context, "Ingreso", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(context, "No se pudo ingrear, Intente Nuevamente", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void setUpCreate(String email, String password) {
		firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					Toast.makeText(context, "Se creo correctamente la cuenta, puedes Iniciar Sesion", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "No se pudo crear la cuenta, intente nuevamente", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		firebaseAuth.addAuthStateListener(authStateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		firebaseAuth.removeAuthStateListener(authStateListener);
	}
}
