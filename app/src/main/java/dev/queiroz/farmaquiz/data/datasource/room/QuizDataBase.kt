package dev.queiroz.farmaquiz.data.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.queiroz.farmaquiz.data.TableNames.DATABASE_NAME
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.model.Question

@Database(
    entities = [
        Category::class,
        Question::class,
        Answer::class,
        CategoryScore::class,
        Player::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDataBase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun playerDao(): PlayerDao
    abstract fun categoryScoreDao(): CategoryScoreDao

    companion object {
        @Volatile
        private var Instance: QuizDataBase? = null

        fun getDatabase(context: Context): QuizDataBase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, QuizDataBase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}