package dev.queiroz.farmaquiz.ui.screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface HomeState {
    object LoadingState : HomeState

    data class LoadedState(
        var userName: String,
        var categories: Flow<List<Category>>,
        var categoriesScores: Flow<List<CategoryWithCategoryScore>>,
    ) : HomeState

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesDataStoreRepository,
    categoryRepository: CategoryRepository,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
    private val playerRepository: PlayerRepository,
    private val categoryScoreRepository: CategoryScoreRepository,
    private val firestoreQuizDataSource: FirestoreQuizDataSource
) : ViewModel() {

    init {
        viewModelScope.launch {
            async {
                userPreferencesRepository
                    .userPreferencesFlow
                    .collectLatest {
                        _userPreferences.postValue(it)
                        _state.tryEmit(
                            HomeState.LoadedState(
                                it.userName,
                                categories = categories,
                                categoriesScores = categoriesWithScores
                            )
                        )
                    }
            }
        }
    }

    private val _userPreferences = MutableLiveData<UserPreferences>(null)
    val userPreferences: LiveData<UserPreferences> = _userPreferences

    val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    private val categories: Flow<List<Category>> = categoryRepository.getAllStream()

    val categoriesWithScores: Flow<List<CategoryWithCategoryScore>> =
        categoryScoreRepository.getAllStream()


    private val _state = MutableStateFlow<HomeState>(HomeState.LoadingState)
    val state: StateFlow<HomeState> = _state

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
            _state.emit(
                HomeState.LoadedState(
                    userPreferences.value!!.userName,
                    categories = categories,
                    categoriesScores = categoriesWithScores
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

    fun updateUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateUserPreferences(userPreferences = userPreferences)
        }
    }
}