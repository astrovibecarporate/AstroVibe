import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.ui.AstrologerViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp


private val LightSaffron = Color(0xFFFFF4E1) // pale saffron

@Composable
fun AstrologerListScreen() {
    val viewModel: AstrologerViewModel = hiltViewModel()
    val astrologersState: Resource<List<Astrologer>> = viewModel.astrologers.collectAsState().value

    var selectedFilter by remember { mutableStateOf("All") }
    var lastClickedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Love", "Education", "Career", "Marriage", "Health", "Wealth")

    val filteredList = when (selectedFilter) {
        "Love" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("vedic", true) || it.knowledge.contains("torat", true)
        } ?: emptyList()
        "Marriage" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("vedic", true) || it.knowledge.contains("palmistry", true)
        } ?: emptyList()
        "Career" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("numerology", true) || it.knowledge.contains("vedic", true)
        } ?: emptyList()
        "Education" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("numerology", true) || it.knowledge.contains("vedic", true) || it.knowledge.contains("torat", true)
        } ?: emptyList()
        "Health" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("palmistry", true) || it.knowledge.contains("vedic", true)
        } ?: emptyList()
        "Wealth" -> (astrologersState as? Resource.Success)?.data?.filter {
            it.knowledge.contains("palmistry", true) || it.knowledge.contains("numerology", true) || it.knowledge.contains("vedic", true)
        } ?: emptyList()
        else -> (astrologersState as? Resource.Success)?.data ?: emptyList()
    }

    val filterIcons = mapOf(
        "All" to Icons.Default.Star,
        "Love" to Icons.Default.Favorite,
        "Education" to Icons.Default.School,
        "Career" to Icons.Default.Work,
        "Marriage" to Icons.Default.FavoriteBorder,
        "Health" to Icons.Default.LocalHospital,
        "Wealth" to Icons.Default.AttachMoney
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightSaffron)
            .padding(16.dp) // You can also pass padding as param if coming from Scaffold
    ) {
        LazyRow(modifier = Modifier.padding(bottom = 8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        if (selectedFilter == filter && lastClickedFilter == filter) {
                            selectedFilter = "All"
                            lastClickedFilter = "All"



                        } else {
                            lastClickedFilter = filter
                            selectedFilter = filter
                        }
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = filterIcons[filter] ?: Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(filter)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        when (astrologersState) {
            is Resource.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredList) { astro ->
                        AstrologerItem(astro)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            is Resource.Error -> {
                val message = astrologersState.message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = Color.Red)
                }
            }
        }
    }
}






@Composable
fun AstrologerItem(astro: Astrologer) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    ) {
                        Spacer(modifier = Modifier.height(15.dp))
                        if (!astro.photourl.isNullOrEmpty()) {
                            // Your actual image loading here (AsyncImage)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray, CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray, CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))

                    StarRating(rating = astro.rating, modifier = Modifier.padding(bottom = 4.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(astro.name, fontWeight = FontWeight.Bold , fontSize = 18.sp )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (astro.verified) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(astro.knowledge)
                    Text("Exp: ${astro.experience} Years")
                    Text("Price: ₹${astro.price}/min")
                    Text("Languages: ${astro.language}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Row() {
                        Text(astro.orders)
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { /* TODO: handle chat click */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(6.dp),   // less round corners
                            modifier = Modifier
                                .align(Alignment.Bottom)
                        ) {
                            Text(text = "Chat", color = Color.White)
                        }
                    }
                }
            }


        }
    }
}

// Example StarRating with proper size so stars don't get cut off
@Composable
fun StarRating(rating: Double, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        val fullStars = rating.toInt()                      // Integer part
        val hasHalfStar = (rating - fullStars) >= 0.5      // Half star if decimal >= 0.5

        repeat(5) { index ->
            when {
                index < fullStars -> {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                index == fullStars && hasHalfStar -> {
                    Icon(
                        imageVector = Icons.Default.StarHalf,  // Use half star icon
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
