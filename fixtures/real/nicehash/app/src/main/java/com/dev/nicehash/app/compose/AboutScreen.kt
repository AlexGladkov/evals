package com.dev.nicehash.app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.nicehash.R

@Composable
fun AboutScreen(
    appVersion: String,
    appBuild: String,
    isDark: Boolean,
    onBackClick: () -> Unit,
    onWebsiteClick: () -> Unit,
    onTwitterClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onDeveloperClick: () -> Unit,
    onSaverClick: () -> Unit
) {
    val bgPrimary = if (isDark) R.color.dark_background_primary else R.color.light_background_primary
    val bgSecondary = if (isDark) R.color.dark_background_secondary else R.color.light_background_secondary
    val textPrimary = if (isDark) R.color.dark_text_color_primary else R.color.light_text_color_primary
    val dividerColor = if (isDark) R.color.dark_divider_color else R.color.light_divider_color
    val headerRes = if (isDark) R.drawable.dark_about_header else R.drawable.light_about_header

    val textSecondary = R.color.light_text_color_secondary // same alpha composite
    val textHint = R.color.text_color_hint

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = bgPrimary))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ── Header ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(296.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = headerRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.header_glow),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 62.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 62.dp)
                        .size(88.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(top = 170.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = textPrimary),
                    letterSpacing = (-0.02).sp
                )
                Text(
                    text = stringResource(id = R.string.about_subline),
                    modifier = Modifier.padding(top = 203.dp),
                    fontSize = 14.sp,
                    color = colorResource(id = textHint),
                    lineHeight = 15.sp
                )
            }

            // ── Content card ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 216.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(colorResource(id = bgSecondary))
            ) {
                SectionLabel(
                    text = stringResource(id = R.string.about_learn_more),
                    colorRes = textSecondary
                )

                CardSection(dividerColorRes = dividerColor, bgPrimaryRes = bgPrimary) {
                    ClickableRow(
                        iconRes = R.drawable.ic_icons_settings_browser,
                        title = stringResource(id = R.string.about_website),
                        subtitle = stringResource(id = R.string.about_website_subtitle),
                        textPrimaryRes = textPrimary,
                        textHintRes = textHint,
                        onClick = onWebsiteClick
                    )
                    DividerLine(colorRes = dividerColor, startIndentDp = 72)
                    ClickableRow(
                        iconRes = R.drawable.ic_icons_settings_avatar,
                        title = stringResource(id = R.string.about_twitter),
                        subtitle = stringResource(id = R.string.about_twitter_subtitle),
                        textPrimaryRes = textPrimary,
                        textHintRes = textHint,
                        onClick = onTwitterClick
                    )
                }

                SectionLabel(
                    text = stringResource(id = R.string.about_authors),
                    colorRes = textSecondary
                )

                CardSection(dividerColorRes = dividerColor, bgPrimaryRes = bgPrimary) {
                    ClickableRow(
                        iconRes = R.drawable.ic_icons_settings_avatar,
                        title = stringResource(id = R.string.about_client),
                        subtitle = stringResource(id = R.string.about_client_subtitle),
                        textPrimaryRes = textPrimary,
                        textHintRes = textHint,
                        avatarMode = true,
                        onClick = onAuthorClick
                    )
                    DividerLine(colorRes = dividerColor, startIndentDp = 72)
                    ClickableRow(
                        iconRes = R.drawable.ic_icons_settings_avatar,
                        title = stringResource(id = R.string.about_author),
                        subtitle = stringResource(id = R.string.about_author_subtitle),
                        textPrimaryRes = textPrimary,
                        textHintRes = textHint,
                        avatarMode = true,
                        onClick = onDeveloperClick
                    )
                }

                SectionLabel(
                    text = stringResource(id = R.string.about_apps),
                    colorRes = textSecondary
                )

                CardSection(dividerColorRes = dividerColor, bgPrimaryRes = bgPrimary) {
                    ClickableRow(
                        iconRes = R.drawable.ic_icons_settings_avatar,
                        title = stringResource(id = R.string.about_apps_saver),
                        subtitle = stringResource(id = R.string.about_apps_saver_subtitle),
                        textPrimaryRes = textPrimary,
                        textHintRes = textHint,
                        avatarMode = true,
                        onClick = onSaverClick
                    )
                }

                // Version text
                Text(
                    text = "${stringResource(id = R.string.app_version)} $appVersion, " +
                            "${stringResource(id = R.string.app_build).lowercase()} $appBuild",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 24.dp)
                        .alpha(0.6f),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = colorResource(id = textHint)
                )
            }
        }

        // ── Back button ──
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 24.dp, start = 4.dp)
                .size(56.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_is_control_back),
                contentDescription = stringResource(id = R.string.about_back),
                tint = colorResource(id = R.color.light_tint_color),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ── Private composables ──

@Composable
private fun SectionLabel(text: String, colorRes: Int) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 16.dp, top = 18.dp, bottom = 8.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(id = colorRes)
    )
}

@Composable
private fun CardSection(
    dividerColorRes: Int,
    bgPrimaryRes: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp)
            .background(colorResource(id = bgPrimaryRes))
    ) {
        DividerLine(colorRes = dividerColorRes, startIndentDp = 0)
        content()
        DividerLine(colorRes = dividerColorRes, startIndentDp = 0)
    }
}

@Composable
private fun ClickableRow(
    iconRes: Int,
    title: String,
    subtitle: String,
    textPrimaryRes: Int,
    textHintRes: Int,
    avatarMode: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = if (avatarMode) 16.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 11.5.dp, bottom = 11.5.dp)
                .size(40.dp)
                .then(if (avatarMode) Modifier.clip(CircleShape) else Modifier),
            // Note: tint is not applied to avatar images in original XML layout
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = colorResource(id = textPrimaryRes)
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = colorResource(id = textHintRes),
                modifier = Modifier.padding(top = 1.dp)
            )
        }
    }
}

@Composable
private fun DividerLine(colorRes: Int, startIndentDp: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startIndentDp.dp)
            .height(1.dp)
            .background(colorResource(id = colorRes))
    )
}
