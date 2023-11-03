import androidx.compose.runtime.mutableStateOf
import dev.queiroz.farmaquiz.data.datasource.firestore.FirestoreQuizDataSource
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.PlayerRepository
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository
import dev.queiroz.farmaquiz.model.Player
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import dev.queiroz.farmaquiz.ui.screen.viewmodel.HomeState
import dev.queiroz.farmaquiz.ui.screen.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDate

const val userName = "Igor Queiroz"

@RunWith(JUnit4::class)
class MainViewModelTest {

    private val userPreferencesRepository: UserPreferencesDataStoreRepository = mock()
    private val categoryRepository: CategoryRepository = mock()
    private val playerRepository: PlayerRepository = mock()
    private val categoryScoreRepository: CategoryScoreRepository = mock()
    private val firestoreQuizDataSource: FirestoreQuizDataSource = mock()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        mainViewModel = MainViewModel(
            userPreferencesRepository,
            categoryRepository,
            playerRepository,
            categoryScoreRepository,
            firestoreQuizDataSource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onFinishWelcomeScreen_shouldCreateNewPlayerObject() = runTest {
        mainViewModel.onFinishWelcomeScreen(userName)
        verify(playerRepository).insert(player = Player(name = userName.trim()))
    }

    @Test
    fun onFinishWelcomeScreen_shouldUpdateTheUserPreferences() = runTest {
        mainViewModel.onFinishWelcomeScreen(userName)
        verify(userPreferencesRepository).updateUserPreferences(
            userPreferences = UserPreferences(
                userName = userName,
                themeMode = ThemeMode.AUTO,
                isFirstLaunch = false,
                LocalDate.now()
            )
        )
    }

    @Test
    fun onFinishWelcomeScreen_shouldEmitTheCorrectState() = runTest {
        val state = mutableStateOf<HomeState>(HomeState.LoadingState)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            mainViewModel.state.collect {
                state.value = it
            }
        }
        mainViewModel.onFinishWelcomeScreen(userName)

        println("State value is: ${state.value}}")
        assert(state.value is HomeState.LoadedState)
    }
}