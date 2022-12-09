package com.syafei.mynavdrawer

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syafei.mynavdrawer.ui.theme.MyNavDrawerTheme


//with state hoisting pattern; like viewModel may be,
// which moving the state to MyNavDrawerState so that a Composable becomes stateless
@Composable //stateless Composable with state hoisting pattern
fun MyNavDrawerApp() {
    val appState = rememberMyNavDrawerState()

    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = {
            MyTopBar(
                onMenuClick = appState::onMenuClick
            )
        },
        drawerContent = {
            MyDrawerContent(
                onItemSelected = appState::onItemSelected,
                onBackPressed = appState::onBackPress,
            )
        },
        drawerGesturesEnabled = appState.scaffoldState.drawerState.isOpen,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(id = R.string.hello_world))
        }
    }
}

@Composable
fun MyTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onMenuClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.menu)
                )
            }
        },
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
    )
}

data class MenuItemNavDraw(val title: String, val icon: ImageVector)

@Composable
fun MyDrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val items = listOf(
        MenuItemNavDraw(
            title = stringResource(id = R.string.home),
            icon = Icons.Default.Home
        ),
        MenuItemNavDraw(
            title = stringResource(id = R.string.favourite),
            icon = Icons.Default.Favorite
        ),
        MenuItemNavDraw(
            title = stringResource(id = R.string.profile),
            icon = Icons.Default.AccountCircle
        ),
    )
    Column(modifier.fillMaxSize()) {
        /*Box(
            modifier = Modifier
                .height(190.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colors.primary
                )
        )*/

        Image(
            modifier = Modifier
                .height(190.dp)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colors.primary
                ),
            painter = painterResource(id = R.drawable.kapallaut),
            contentDescription = "image",
            contentScale = ContentScale.Crop
        )

        for (item in items) {
            Row(
                modifier = Modifier
                    .clickable { onItemSelected(item.title) }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = item.title, style = MaterialTheme.typography.subtitle2)
            }
        }
        Divider()
    }

    BackPressHandler {
        onBackPressed()
    }
}

@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val backCallBack = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }

        }
    }

    SideEffect {
        backCallBack.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifeCycleOwner, backCallBack)
        onDispose {
            backCallBack.remove()
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    MyNavDrawerTheme {
        MyNavDrawerApp()
    }
}




























