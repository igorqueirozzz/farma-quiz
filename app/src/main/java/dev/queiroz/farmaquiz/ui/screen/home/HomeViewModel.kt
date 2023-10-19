package dev.queiroz.farmaquiz.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.queiroz.farmaquiz.data.datasource.firestore.FirestoreQuizDataSource
import dev.queiroz.farmaquiz.data.repository.AnswerRepository
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.PlayerRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface HomeState {
    object LoadingState : HomeState

    data class LoadedState(
        var categories: Flow<List<Category>>,
        var categoriesScores: Flow<List<CategoryScore>>,
        val player: Flow<Player>
    ) : HomeState

}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesDataStoreRepository,
    categoryRepository: CategoryRepository,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val playerRepository: PlayerRepository,
    private val categoryScoreRepository: CategoryScoreRepository,
    private val firestoreQuizDataSource: FirestoreQuizDataSource
) : ViewModel() {

    val userPreferences: LiveData<UserPreferences> =
        userPreferencesRepository.userPreferencesFlow.asLiveData()

    private val player = playerRepository.getPlayerStream()

    private val categories: Flow<List<Category>> = categoryRepository.getAllStream()

    private val categoriesScores: Flow<List<CategoryScore>> = categoryScoreRepository.getAllStream()

    var state: StateFlow<HomeState> = MutableStateFlow(HomeState.LoadingState)
        private set
        get() {
            return if (userPreferences.value == null)
                MutableStateFlow(HomeState.LoadingState)
            else MutableStateFlow(
                HomeState.LoadedState(
                    categories = categories,
                    player = player,
                    categoriesScores = categoriesScores
                )
            )
        }

    fun onFinishWelcomeScreen(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playerRepository.insert(player = Player(name = userName.trim()))
            userPreferencesRepository.updateUserPreferences(
                userPreferences = UserPreferences(
                    userName = userName,
                    themeMode = ThemeMode.AUTO,
                    isFirstLaunch = false,
                    LocalDate.now()
                )
            )
        }
    }

    fun updateDatabase(onUpdateComplete: (Boolean) -> Unit) {
        firestoreQuizDataSource.updateCategoriesWithFirestoreData { success ->
            onUpdateComplete(success)
            viewModelScope.launch {
                userPreferences.value.let {
                    it!!
                    userPreferencesRepository.updateUserPreferences(
                        userPreferences = UserPreferences(
                            userName = it.userName,
                            themeMode = it.themeMode,
                            isFirstLaunch = it.isFirstLaunch,
                            lastDataUpdate = LocalDate.now()
                        )
                    )
                }
            }
        }

        firestoreQuizDataSource.updateQuestionsWithFirestoreData { }

        firestoreQuizDataSource.updateAnswersWithFirestoreData { }
    }
}