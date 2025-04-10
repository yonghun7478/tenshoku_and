package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.UserData
import com.example.tokitoki.ui.viewmodel.AshiatoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageAshiatoScreen(viewModel: AshiatoViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "아시아토",
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    )
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 8.dp + paddingValues.calculateTopPadding(),
                end = 16.dp,
                bottom = 8.dp + paddingValues.calculateBottomPadding()
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.users) { user ->
                MyPageAshiatoListItem(
                    user = user,
                    onUserClicked = { viewModel.onUserClicked(user.id) })
            }
        }
    }
}

@Composable
fun MyPageAshiatoListItem(user: UserData, onUserClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        // Visit Information (Top-Left)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.female_thinking), // Replace with your footprint icon
                contentDescription = "Footprint",
                tint = Color(0xFFAAAAAA),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${user.daysAgoVisited}일 전",
                color = Color(0xFF888888),
                fontSize = 12.sp
            )
        }

        // Profile Photo (Center)
        Box {
            Image(
                painter = rememberImagePainter(data = user.profileImageUrl),
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            // Like Icon (Bottom-Right Overlay)
            Icon(
                painter = painterResource(id = R.drawable.female_thinking), // Replace with your like icon
                contentDescription = "Like",
                tint = Color(0xFFFF5252),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(24.dp)
            )
        }

        // User Information (Below Photo)
        Text(
            text = "${user.age}세, ${user.location}",
            color = Color(0xFF212121),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageAshiatoScreenPreview() {
    MyPageAshiatoScreen()
}

@Preview(showBackground = true)
@Composable
fun MyPageAshiatoListItemPreview() {
    val dummyUser = UserData(
        id = "1",
        profileImageUrl = "https://source.unsplash.com/random/300x300?face&1",
        daysAgoVisited = 1,
        age = 25,
        location = "서울"
    )
    MyPageAshiatoListItem(user = dummyUser, onUserClicked = {})
}