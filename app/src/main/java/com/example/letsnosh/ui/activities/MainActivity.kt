package com.example.letsnosh.ui.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.letsnosh.R
import com.example.letsnosh.data.Dish
import com.example.letsnosh.ui.composables.CookingTimeBottomSheet
import com.example.letsnosh.ui.theme.Blue
import com.example.letsnosh.ui.theme.DarkBlue
import com.example.letsnosh.ui.theme.LightOrange
import com.example.letsnosh.ui.theme.Orange
import com.example.letsnosh.viewmodel.DishViewModel

class MainActivity : ComponentActivity() {

    private val dishViewModel: DishViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dishViewModel.loadDishes()
        setContent {
            DishApp(dishViewModel = dishViewModel)
        }
    }
}

@Composable
fun DishApp(dishViewModel: DishViewModel) {
    val activeRecipeIndex = rememberSaveable { mutableIntStateOf(-1) }
    val selectTimeForCooking = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    if (selectTimeForCooking.value && activeRecipeIndex.intValue != -1) {
        CookingTimeBottomSheet(
            onDeleteClick = { stayTunedToastMessage(context) },
            onRescheduleClick = { stayTunedToastMessage(context) },
            onCookNowClick = { stayTunedToastMessage(context) },
            onDismissRequest = { selectTimeForCooking.value = false },
        )
    }

    DishAppLayout(dishViewModel, activeRecipeIndex, selectTimeForCooking)
}

@Composable
fun DishAppLayout(
    dishViewModel: DishViewModel,
    activeRecipeIndex: MutableIntState,
    selectTimeForCooking: MutableState<Boolean>,
) {
    val error by dishViewModel.error.observeAsState(initial = null)
    val cxt = LocalContext.current

    error?.let {
        LaunchedEffect(it) {
            Toast.makeText(cxt, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold { padding ->
        Row {

            NavigationBar()

            AppContent(
                modifier = Modifier.padding(padding),
                dishViewModel = dishViewModel,
                selectedIndex = activeRecipeIndex,
                showBottomSheet = selectTimeForCooking
            )
        }
    }
}


@Composable
private fun NavigationBar(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current

    NavigationRail(
        modifier = modifier.padding(horizontal = 8.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val navigationBarItems = listOf(
                Triple(Icons.Default.Home, "Cook", true),
                Triple(Icons.Default.FavoriteBorder, "Favorites", false),
                Triple(Icons.Default.Home, "Manual", false),
                Triple(Icons.Default.Phone, "Device", false),
                Triple(Icons.Default.AccountCircle, "Preferences", false),
                Triple(Icons.Default.Settings, "Settings", false)
            )

            navigationBarItems.forEachIndexed { index, (icon, label, isSelected) ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) Orange else Blue
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            color = if (isSelected) Orange else Blue
                        )
                    },
                    selected = isSelected,
                    onClick = { if (isSelected.not()) stayTunedToastMessage(ctx) },
                    colors = NavigationRailItemColors(
                        selectedIconColor = Orange,
                        selectedTextColor = Orange,
                        selectedIndicatorColor = Color.White,
                        unselectedIconColor = Blue,
                        unselectedTextColor = Blue,
                        disabledIconColor = Blue,
                        disabledTextColor = Blue
                    )
                )
            }
        }
    }
}


@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    dishViewModel: DishViewModel,
    selectedIndex: MutableIntState,
    showBottomSheet: MutableState<Boolean>,
) {
    val availableRecipes by dishViewModel.dishes.observeAsState(initial = emptyList())

    Column(modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(16.dp))

        Header()

        Headings(title = R.string.whats_on_your_mind) {
            CategoryCarousel()
        }

        Headings(title = R.string.recommendations) {
            RecommendationsRail(
                dishes = availableRecipes,
                selectedIndex = selectedIndex,
                showBottomSheet = showBottomSheet
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BottomActionButtons()
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SearchBar(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        ScheduledDish(
            dishName = "Italian Spaghetti Pasta",
            onClickNotification = { stayTunedToastMessage(ctx, "No new Notifications.") },
            onClickLogOut = { stayTunedToastMessage(ctx) }
        )
    }
}


@Composable
fun Headings(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    followingComposable: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodyLarge,
            color = Blue,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        followingComposable()
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var queryText by rememberSaveable { mutableStateOf("") }
    val ctx = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current


    TextField(
        modifier = modifier
            .heightIn(min = 56.dp)
            .clip(CircleShape)
            .border(1.dp, Blue, CircleShape),
        value = queryText,
        onValueChange = { queryText = it },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Blue
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(text = stringResource(R.string.search_hint))
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (queryText.isNotBlank()) {
                    stayTunedToastMessage(ctx, "You searched for $queryText")
                }
            },
            onDone = { keyboardController?.hide() }
        )
    )
}

@Composable
fun ScheduledDish(
    modifier: Modifier = Modifier,
    dishName: String = "Italian Spaghetti Pasta",
    scheduleTime: String = "Scheduled 6:30 AM",
    onClickNotification: () -> Unit = {},
    onClickLogOut: () -> Unit = {}
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .background(DarkBlue, shape = CircleShape)
                .padding(horizontal = 8.dp)
        ) {
            Image(
                painterResource(id = R.drawable.spaghetti),
                contentDescription = "Scheduled Recipe Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.padding(end = 4.dp)) {
                Text(
                    text = if (dishName.length > 15) dishName.take(15) + "..." else dishName,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = scheduleTime,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        IconButton(
            modifier = Modifier.padding(start = 10.dp),
            onClick = onClickNotification
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Blue
            )
        }

        IconButton(onClick = onClickLogOut) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Log out",
                tint = Color.Red
            )
        }
    }
}


@Composable
fun CategoryCarousel(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(whatsOnYourMindDishes) { item ->
            CategoryItem(
                drawableId = item.drawable,
                itemName = item.text
            )
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int,
    itemName: String
) {
    Card(
        shape = CircleShape,
        modifier = modifier.shadow(8.dp, CircleShape),
        colors = CardDefaults.cardColors(containerColor = Color(0xfffafafc)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = itemName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(2.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(
                text = itemName,
                style = MaterialTheme.typography.titleMedium,
                color = Blue,
                modifier = Modifier.padding(start = 10.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun RecommendationsRail(
    modifier: Modifier = Modifier,
    dishes: List<Dish>,
    selectedIndex: MutableIntState,
    showBottomSheet: MutableState<Boolean>
) {
    val ratingList = mutableListOf(
        "4.2", "4.7", "5.0", "3.8", "3.5"
    )
    val timeToPrepare = mutableListOf(
        "15 min", "30 min", "90 min", "60 min", "45 min"
    )
    val difficulty = mutableListOf(
        "Easy", "Easy", "Moderate", "Moderately Hard", "Hard"
    )
    val isVegetarian = mutableListOf(
        true, true, true, false, true
    )
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(dishes) { index, item ->
            DishCard(
                item,
                index,
                selectedIndex,
                showBottomSheet,
                ratingList[index],
                timeToPrepare[index],
                difficulty[index],
                isVegetarian[index]
            )
        }
    }
}

@Composable
fun DishCard(
    dish: Dish,
    index: Int,
    selectedIndex: MutableIntState,
    showBottomSheet: MutableState<Boolean>,
    rating: String,
    time: String,
    difficulty: String,
    isVegetarian: Boolean
) {
    val isSelected = index == selectedIndex.intValue
    val backgroundColor = if (isSelected) Blue else Color(0xFFFFFFFF)
    val textColor = if (isSelected) Color.White else Color.Black

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp)
            .clickable {
                showBottomSheet.value = true
                selectedIndex.intValue = if (index == selectedIndex.intValue) -1 else index
            }
            .shadow(4.dp, RoundedCornerShape(16.dp), clip = false),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box {
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color(0xffebebeb))
                ) {
                    AsyncImage(
                        model = dish.imageUrl,
                        contentDescription = dish.dishName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .size(115.dp)
                    )
                    Image(
                        painter = painterResource(id = if (isVegetarian) R.drawable.green_dot else R.drawable.red_dot),
                        contentDescription = if (isVegetarian) "Vegetarian" else "Non-Vegetarian",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 8.dp)
                        .background(
                            color = LightOrange,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 3.dp)
                    )
                    Text(
                        text = rating,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
            }

            Text(
                text = dish.dishName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) textColor else Blue,
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 15.dp, bottom = 5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cooking),
                    contentDescription = "barbeque image",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "$time · $difficulty prep.",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun BottomActionButtons() {
    val ctx = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttonModifier = Modifier
            .weight(1f)
            .height(60.dp)

        val buttonColors = ButtonDefaults.buttonColors(containerColor = Orange)
        val buttonShape = RoundedCornerShape(20.dp)

        Button(
            onClick = { stayTunedToastMessage(ctx) },
            colors = buttonColors,
            shape = buttonShape,
            modifier = buttonModifier.padding(end = 8.dp)
        ) {
            Text(
                text = "Explore all dishes",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { stayTunedToastMessage(ctx) },
            colors = buttonColors,
            shape = buttonShape,
            modifier = buttonModifier.padding(start = 8.dp)
        ) {
            Text(
                text = "Confused what to cook?",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun stayTunedToastMessage(context: Context, message: String = "Stay tuned for further updates!") {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private val whatsOnYourMindDishes = createItemList()

private data class ItemsInMyMind(
    @DrawableRes val drawable: Int,
    val text: String
)

private fun createItemList() = listOf(
    createItem(R.drawable.rice, "Rice Items"),
    createItem(R.drawable.indian, "Indian Flavors"),
    createItem(R.drawable.curries, "Savory Curries"),
    createItem(R.drawable.soup, "Hot Soups"),
    createItem(R.drawable.dessert, "Sweet Treats"),
    createItem(R.drawable.snacks, "Light Bites")
)

private fun createItem(imageRes: Int, title: String): ItemsInMyMind {
    return ItemsInMyMind(imageRes, title)
}