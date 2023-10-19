package dev.queiroz.farmaquiz.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.queiroz.farmaquiz.constants.DataStoreConstants.USER_PREFERENCES
import dev.queiroz.farmaquiz.data.datasource.firestore.FirestoreQuizDataSource
import dev.queiroz.farmaquiz.data.datasource.firestore.FirestoreQuizDataSourceImpl
import dev.queiroz.farmaquiz.data.datasource.room.QuizDataBase
import dev.queiroz.farmaquiz.data.repository.AnswerRepository
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.PlayerRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository
import dev.queiroz.farmaquiz.data.repository.impl.AnswerOfflineRepository
import dev.queiroz.farmaquiz.data.repository.impl.CategoryOfflineRepository
import dev.queiroz.farmaquiz.data.repository.impl.CategoryScoreOfflineRepository
import dev.queiroz.farmaquiz.data.repository.impl.PlayerOfflineRepository
import dev.queiroz.farmaquiz.data.repository.impl.QuestionOfflineRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.dataStore by preferencesDataStore(USER_PREFERENCES)

    @Provides
    @Singleton
    fun provideDataBaseInstance(@ApplicationContext context: Context): QuizDataBase =
        QuizDataBase.getDatabase(context = context)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context):
            UserPreferencesDataStoreRepository =
        UserPreferencesDataStoreRepository(dataStore = context.dataStore)

    @Provides
    @Singleton
    fun provideAnswerRepository(dataBase: QuizDataBase): AnswerRepository =
        AnswerOfflineRepository(answerDao = dataBase.answerDao())

    @Provides
    @Singleton
    fun provideQuestionRepository(dataBase: QuizDataBase): QuestionRepository =
        QuestionOfflineRepository(questionDao = dataBase.questionDao())

    @Provides
    @Singleton
    fun provideCategoryRepository(dataBase: QuizDataBase): CategoryRepository =
        CategoryOfflineRepository(categoryDao = dataBase.categoryDao())

    @Provides
    @Singleton
    fun providePlayerRepository(dataBase: QuizDataBase): PlayerRepository =
        PlayerOfflineRepository(playerDao = dataBase.playerDao())

    @Provides
    @Singleton
    fun providerCategoryScoreRepository(dataBase: QuizDataBase): CategoryScoreRepository =
        CategoryScoreOfflineRepository(categoryScoreDao = dataBase.categoryScoreDao())

    @Provides
    @Singleton
    fun providesFirestoreQuizDataSource(
        categoryRepository: CategoryRepository,
        questionRepository: QuestionRepository,
        answerRepository: AnswerRepository
    ): FirestoreQuizDataSource =
        FirestoreQuizDataSourceImpl(
            categoryRepository = categoryRepository,
            questionRepository = questionRepository,
            answerRepository = answerRepository
        )
}