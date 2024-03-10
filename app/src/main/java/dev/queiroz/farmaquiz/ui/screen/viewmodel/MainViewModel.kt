package dev.queiroz.farmaquiz.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.queiroz.farmaquiz.data.datasource.firestore.FirestoreQuizDataSource
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.PlayerRepository
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository
import dev.queiroz.farmaquiz.data.repository.impl.UserPreferencesDataStoreRepositoryImpl
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import dev.queiroz.farmaquiz.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface HomeState {
    object LoadingState : HomeState

    data class LoadedState(
        var userName: String,
        var categories: Flow<List<Category>>?,
        var categoriesScores: Flow<List<CategoryWithCategoryScore>>?,
    ) : HomeState

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val userPreferencesRepository: UserPreferencesDataStoreRepository,
    categoryRepository: CategoryRepository,
    private val playerRepository: PlayerRepository,
    categoryScoreRepository: CategoryScoreRepository,
    private val firestoreQuizDataSource: FirestoreQuizDataSource
) : ViewModel() {


    val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    private var lastUserPreferences: UserPreferences? = null

    private val categories: Flow<List<Category>> = categoryRepository.getAllStream()

    val categoriesWithScores: Flow<List<CategoryWithCategoryScore>> =
        categoryScoreRepository.getAllStream()


    private val _state = MutableStateFlow<HomeState>(HomeState.LoadingState)
    val state: StateFlow<HomeState> = _state.asStateFlow()


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            userPreferencesRepository
                .userPreferencesFlow
                .collectLatest {
                    lastUserPreferences = it
                    _state.tryEmit(
                        HomeState.LoadedState(
                            it.userName,
                            categories = categories,
                            categoriesScores = categoriesWithScores,
                        )
                    )
                }

        }
    }

    fun onFinishWelcomeScreen(userName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
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
                    userName = userName,
                    categories = categories,
                    categoriesScores = categoriesWithScores
                )
            )
        }
    }

    fun updateDatabase(currentVersion: String, onUpdateComplete: (Boolean) -> Unit) {
        firestoreQuizDataSource.updateCategoriesWithFirestoreData { success ->
            onUpdateComplete(success)
            viewModelScope.launch {
                lastUserPreferences?.let {
                    userPreferencesRepository.updateUserPreferences(
                        userPreferences = it.copy(lastDataUpdate = LocalDate.now(), latestVersion = currentVersion)
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