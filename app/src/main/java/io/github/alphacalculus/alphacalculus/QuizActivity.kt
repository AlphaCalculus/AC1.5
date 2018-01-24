package io.github.alphacalculus.alphacalculus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.concurrent.ThreadLocalRandom

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val quiz_idx = intent.getIntExtra("io.github.alphacalculus.QUIZ_ID", 0)
        val quiz = ChapterItemFactory.getQuiz(quiz_idx)
        val ulInner = (1..quiz.size).map { i ->
            val questionItem = quiz[i]
            val sequence = (0 until questionItem.answers.size).toList().toIntArray()
            shuffleArray(sequence)
            sequence.map {
                "<span>" + (if (questionItem.correctAnswerIdx==it) {
                    "<input type=radio id=qid_${i}_aid_${it} name=qid_${i} data-is-correct=true>"
                } else {
                    "<input type=radio id=qid_${i}_aid_${it} name=qid_${i} >"
                })+"${questionItem.answers[it]}</span>"
            }.joinToString("")
        }.joinToString("")
        val html = """
            <form>
                <ol>
                    ${ulInner}
                </ol>
            </form>
            <script>
                function submitQuiz(self) {
                    var questions = document.querySelectorAll("#quiz li");
                    var wrongIds = [];
                    questions.forEach(function(liQ){
                        var radios = liQ.querySelectorAll('input[type=radio]');
                        var checked = Array.from(radios).filter(function(r){return r.checked;});
                        console.log(checked);
                        if (checked.length!=1 || checked[0].getAttribute('data-is-correct')!='true') {
                            wrongIds.push(liQ.id);
                        }
                    });
                    alert(JSON.stringify(wrongIds));
                }
            </script>
        """.trimIndent()
        webView.loadData(html, "text/html", "utf-8")
    }

    fun shuffleArray(ar: IntArray) {
        val rnd = ThreadLocalRandom.current()
        for (i in ar.size - 1 downTo 1) {
            val index = rnd.nextInt(i + 1)
            // Simple swap
            val a = ar[index]
            ar[index] = ar[i]
            ar[i] = a
        }
    }
}
