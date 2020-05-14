package maksymilianstrzyzewski.com;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CORRECT_ANSWERS = "correct_answers";
    private static final String KEY_TOTAL_ANSWERS = "total_answers";
    private static final String KEY_QUESTION_BANK = "question_bank";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true, false),
            new Question(R.string.question_oceans, true, false),
            new Question(R.string.question_mideast, false, false),
            new Question(R.string.question_africa, false, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia, true, false),
    };
    private int mCurrentIndex = 0;
    private int mNumberOfCorrectAnswers = 0;
    private int mNumberOfTotalAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Wywołanie metody: onCreate(Bundle)");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mNumberOfCorrectAnswers = savedInstanceState.getInt(KEY_CORRECT_ANSWERS, 0);
            mNumberOfTotalAnswers = savedInstanceState.getInt(KEY_TOTAL_ANSWERS, 0);
            mQuestionBank = (Question[]) savedInstanceState.getSerializable(KEY_QUESTION_BANK);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                }
                updateQuestion();
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Wywołanie metody: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołanie metody: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołanie metody: onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "Wywołanie metody: onSaveInstanceState(Bundle)");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_CORRECT_ANSWERS, mNumberOfCorrectAnswers);
        savedInstanceState.putInt(KEY_TOTAL_ANSWERS, mNumberOfTotalAnswers);
        savedInstanceState.putSerializable(KEY_QUESTION_BANK, mQuestionBank);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołanie metody: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołanie metody: onDestroy()");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            changeButtonsState(false);
        } else {
            changeButtonsState(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mNumberOfCorrectAnswers++;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        mQuestionBank[mCurrentIndex].setAnswered(true);
        changeButtonsState(false);
        mNumberOfTotalAnswers++;
        checkQuizState();
    }

    private void changeButtonsState(boolean enabled) {
        mTrueButton.setEnabled(enabled);
        mFalseButton.setEnabled(enabled);
    }

    private void checkQuizState() {
        if (mNumberOfTotalAnswers == mQuestionBank.length) {
            double scorePercent = (double) mNumberOfCorrectAnswers / mQuestionBank.length * 100;
            String message = getString(R.string.score, mNumberOfCorrectAnswers, "(" + new DecimalFormat("##.##").format(scorePercent)) + "%)";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
