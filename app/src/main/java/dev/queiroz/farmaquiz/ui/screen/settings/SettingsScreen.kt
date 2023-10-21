package dev.queiroz.farmaquiz.ui.screen.settings

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.drawable.toBitmap
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import dev.queiroz.farmaquiz.ui.Settings
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userPreferences: UserPreferences,
    onSaveUserPreferences: (UserPreferences) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val myUserPreferences by remember {
        mutableStateOf(userPreferences)
    }
    var newName by remember {
        mutableStateOf(userPreferences.userName)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (!newName.isNullOrBlank()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        myUserPreferences.userName = newName
                        onSaveUserPreferences(myUserPreferences)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = context.getString(R.string.preferences_saved)
                            )
                        }
                    },
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 32.dp)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserPhotoPicker()

            TextField(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
                value = newName,
                onValueChange = { value ->
                    newName = value
                },
                label = {
                    Text(text = stringResource(R.string.name))
                })

            Column {
                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 6.dp)
                    .clickable {
                        isMenuExpanded = true
                    },
                    value = myUserPreferences.themeMode.getDescription(context = LocalContext.current),
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(text = stringResource(R.string.theme))
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable { isMenuExpanded = !isMenuExpanded },
                            imageVector = if (isMenuExpanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                            contentDescription = null
                        )
                    })

                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.auto)) },
                        onClick = {
                            myUserPreferences.themeMode = ThemeMode.AUTO
                            isMenuExpanded = false
                        })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.light)) },
                        onClick = {
                            myUserPreferences.themeMode = ThemeMode.LIGHT
                            isMenuExpanded = false
                        })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.dark)) },
                        onClick = {
                            myUserPreferences.themeMode = ThemeMode.DARK
                            isMenuExpanded = false
                        })
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.dynamic)) },
                            onClick = {
                                myUserPreferences.themeMode = ThemeMode.DYNAMIC
                                isMenuExpanded = false
                            })
                    }
                }
            }


        }
    }
}

@Composable
fun UserPhotoPicker(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var imageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    saveProfilePicture(context = context, uri = uri)
                    imageBitmap = null
                }
            })

    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 30.dp
        )
    ) {
        Box(
            modifier = modifier.size(160.dp)
        ) {


            if (imageBitmap == null) {
                LaunchedEffect(key1 = Unit) {
                    imageBitmap = Settings.getProfilePicture(context = context)
                }
            }

            if (imageBitmap != null) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.training_brain),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            Box(modifier = Modifier
                .clickable { launcher.launch("image/*") }
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 6.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UserPhotoPickerPreview() {
    FarmaQuizTheme {
        UserPhotoPicker()
    }
}

@Composable
@Preview(showSystemUi = true)
fun SettingsScreenPreview() {
    FarmaQuizTheme {
        SettingsScreen(userPreferences = UserPreferences(
            userName = "preview",
            themeMode = ThemeMode.AUTO,
            isFirstLaunch = false,
            lastDataUpdate = null,
        ), onSaveUserPreferences = {})
    }
}

fun Settings.getProfilePicture(context: Context): Bitmap {
    return with(Dispatchers.IO) {
        val file =
            context.filesDir.listFiles()?.find { it.canRead() && it.name == "profile_photo.jpeg" }
        if (file != null) {
            val bytes = file.readBytes()
            return@with BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else {
            return@with context.getDrawable(
                R.drawable.training_brain,
            )!!.toBitmap()
        }
    }
}

private fun saveProfilePicture(context: Context, uri: Uri) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput("profile_photo.jpeg", Context.MODE_PRIVATE)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}

private fun ThemeMode.getDescription(context: Context): String {
    return when (this) {
        ThemeMode.AUTO -> context.getString(R.string.auto)
        ThemeMode.LIGHT -> context.getString(R.string.light)
        ThemeMode.DARK -> context.getString(R.string.dark)
        else -> context.getString(R.string.dynamic)
    }
}