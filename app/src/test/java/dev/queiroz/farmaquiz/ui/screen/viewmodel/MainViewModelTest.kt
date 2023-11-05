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
import dev.queiroz.farmaquiz.utils.TestDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.atMost
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

const val userName = "Igor Queiroz"


@RunWith(MockitoJUnitRunner::class)
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
            dispatcherProvider = TestDispatcherProvider(),
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
    fun onFinishWelcomeScreen_shouldCreateNewPlayerObject() = runBlocking {
        mainViewModel.onFinishWelcomeScreen(userName)
        verify(playerRepository).insert(player = Player(name = userName.trim()))
    }

    @Test
    fun onFinishWelcomeScreen_shouldUpdateTheUserPreferences() = runTest {
        mainViewModel.onFinishWelcomeScreen(userName)
        advanceUntilIdle()

        verify(userPreferencesRepository, atMost(2)).updateUserPreferences(
            userPreferences = UserPreferences(
                userName = userName,
                themeMode = ThemeMode.AUTO,
                isFirstLaunch = false,
                lastDataUpdate = null,
            )
        )
    }

    @Test
    fun onFinishWelcomeScreen_shouldEmitTheCorrectState() {
        runBlocking {
            val state = mutableListOf<HomeState>()

            val job = launch(UnconfinedTestDispatcher()) {
                mainViewModel.state.toList(state)
            }

            mainViewModel.onFinishWelcomeScreen(userName)

            assert(state.last() is HomeState.LoadedState)
            job.cancel()
        }
    }

}