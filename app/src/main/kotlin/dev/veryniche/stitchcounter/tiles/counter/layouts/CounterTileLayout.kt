package dev.veryniche.stitchcounter.tiles.counter.layouts

import CounterTileRenderer
import CounterTileRenderer.Companion.BUTTON_EXTRA_SMALL_SIZE
import CounterTileRenderer.Companion.BUTTON_SMALL_SIZE
import CounterTileRenderer.Companion.ID_IC_ADD
import CounterTileRenderer.Companion.ID_IC_EDIT
import CounterTileRenderer.Companion.ID_IC_REMOVE
import CounterTileRenderer.Companion.ID_IC_RESET
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.ui.graphics.toArgb
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.wrap
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.expression.ProtoLayoutExperimental
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonDefaults
import androidx.wear.protolayout.material.CircularProgressIndicator
import androidx.wear.protolayout.material.ProgressIndicatorColors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.EdgeContentLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.tooling.preview.TilePreviewData
import dev.veryniche.stitchcounter.data.models.Counter
import dev.veryniche.stitchcounter.data.models.Project
import dev.veryniche.stitchcounter.getCounterProgress
import dev.veryniche.stitchcounter.presentation.theme.stitchCounterColorPalette
import dev.veryniche.stitchcounter.previews.PreviewTile
import dev.veryniche.stitchcounter.tiles.counter.CounterTileState
import dev.veryniche.stitchcounter.tiles.counter.StitchCounterTileTheme
import previewResources

@OptIn(ProtoLayoutExperimental::class)
@Suppress("LongParameterList")
fun counterTileLayout(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
    projectName: String,
    counter: Counter,
    clickablePositive: ModifiersBuilders.Clickable,
    clickableNegative: ModifiersBuilders.Clickable,
    clickableReset: ModifiersBuilders.Clickable,
    clickableEdit: ModifiersBuilders.Clickable,
): LayoutElementBuilders.LayoutElement {
    val showProgress = counter.maxCount > 0

    val extraSmallButtons = (showProgress &&
        deviceParameters.screenWidthDp < 195 &&
        deviceParameters.fontScale >= 1.0f) //|| (showProgress && deviceParameters.fontScale >= 1.3f)

    return if (showProgress) {
        counterWithProgress(
            context = context,
            deviceParameters = deviceParameters,
            extraSmallButtons = extraSmallButtons,
            projectName = projectName,
            counter = counter,
            clickablePositive = clickablePositive,
            clickableNegative = clickableNegative,
            clickableReset = clickableReset,
            clickableEdit = clickableEdit
        )
    } else {
        counterWithoutProgress(
            context = context,
            deviceParameters = deviceParameters,
            extraSmallButtons = extraSmallButtons,
            projectName = projectName,
            counter = counter,
            clickablePositive = clickablePositive,
            clickableNegative = clickableNegative,
            clickableReset = clickableReset,
            clickableEdit = clickableEdit
        )
    }.build()
}

@Suppress("LongParameterList")
fun counterWithoutProgress(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
    projectName: String,
    counter: Counter,
    clickablePositive: ModifiersBuilders.Clickable,
    clickableNegative: ModifiersBuilders.Clickable,
    clickableReset: ModifiersBuilders.Clickable,
    clickableEdit: ModifiersBuilders.Clickable,
    extraSmallButtons: Boolean,
) = PrimaryLayout.Builder(deviceParameters)
    .setResponsiveContentInsetEnabled(true)
    .setPrimaryLabelTextContent(
        counterPrimaryLabelContent(context, counter, projectName)
    )
    .setContent(
        counterMainContent(context, counter, extraSmallButtons, clickablePositive, clickableNegative)
    )
    .setSecondaryLabelTextContent(
        counterSecondaryLabelContent(context, clickableReset, clickableEdit)
    )

@Suppress("LongParameterList")
fun counterWithProgress(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
    projectName: String,
    counter: Counter,
    clickablePositive: ModifiersBuilders.Clickable,
    clickableNegative: ModifiersBuilders.Clickable,
    clickableReset: ModifiersBuilders.Clickable,
    clickableEdit: ModifiersBuilders.Clickable,
    extraSmallButtons: Boolean,
) = EdgeContentLayout.Builder(deviceParameters)
    .setResponsiveContentInsetEnabled(true)
    .setEdgeContentThickness(5f)
    .setEdgeContent(
        CircularProgressIndicator.Builder()
            .setProgress(counter.getCounterProgress() ?: 0f)
            .setStartAngle(40f)
            .setEndAngle(320f)
            .setStrokeWidth(3f)
            .setCircularProgressIndicatorColors(
                ProgressIndicatorColors(
                    argb(StitchCounterTileTheme.colors.primary),
                    argb(stitchCounterColorPalette.secondaryVariant.toArgb())
                )
            )
            .build()
    )
    .setPrimaryLabelTextContent(
        counterPrimaryLabelContent(context, counter, projectName)
    )
    .setContent(
        counterMainContent(context, counter, extraSmallButtons, clickablePositive, clickableNegative)
    )
    .setSecondaryLabelTextContent(
        counterSecondaryLabelContent(context, clickableReset, clickableEdit)
    )

@OptIn(ProtoLayoutExperimental::class)
fun counterPrimaryLabelContent(
    context: Context,
    counter: Counter,
    projectName: String,
) = LayoutElementBuilders.Column.Builder()
    .setWidth(wrap())
    .setHeight(wrap())
    .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
    .addContent(
        Text.Builder(context, projectName)
            .setTypography(Typography.TYPOGRAPHY_CAPTION2)
            .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
            .setColor(argb(stitchCounterColorPalette.onSecondary.toArgb()))
            .build()
    ).addContent(
        Text.Builder(context, counter.name)
            .setTypography(Typography.TYPOGRAPHY_CAPTION1)
            .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
            .setColor(argb(stitchCounterColorPalette.primaryVariant.toArgb()))
            .build()
    ).build()

fun counterSecondaryLabelContent(
    context: Context,
    clickableReset: ModifiersBuilders.Clickable,
    clickableEdit: ModifiersBuilders.Clickable,
) = LayoutElementBuilders.Row.Builder()
    .setWidth(wrap())
    .setHeight(wrap())
    .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
    .addContent(
        Button.Builder(context, clickableReset)
            .setContentDescription("Reset counter")
            .setIconContent(ID_IC_RESET)
            .setSize(BUTTON_EXTRA_SMALL_SIZE)
            .setButtonColors(ButtonDefaults.SECONDARY_COLORS)
            .build()
    )
    .addContent(
        LayoutElementBuilders.Spacer.Builder()
            .setWidth(dp(2f))
            .build()
    )
    .addContent(
        Button.Builder(context, clickableEdit)
            .setContentDescription("Edit counter")
            .setIconContent(ID_IC_EDIT)
            .setSize(BUTTON_EXTRA_SMALL_SIZE)
            .setButtonColors(ButtonDefaults.SECONDARY_COLORS)
            .build()
    )
    .build()

@OptIn(ProtoLayoutExperimental::class)
@Suppress("LongParameterList")
fun counterMainContent(
    context: Context,
    counter: Counter,
    extraSmallButtons: Boolean,
    clickablePositive: ModifiersBuilders.Clickable,
    clickableNegative: ModifiersBuilders.Clickable,
) = LayoutElementBuilders.Box.Builder()
    .setHorizontalAlignment(LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER)
    .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
    .addContent(
        LayoutElementBuilders.Row.Builder()
            .setWidth(wrap())
            .setHeight(wrap())
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .addContent(
                Button.Builder(context, clickableNegative)
                    .setContentDescription("Subtract 1")
                    .setIconContent(ID_IC_REMOVE)
                    .setSize(
                        if (extraSmallButtons) {
                            BUTTON_EXTRA_SMALL_SIZE
                        } else {
                            BUTTON_SMALL_SIZE
                        }
                    )
                    .setButtonColors(ButtonDefaults.SECONDARY_COLORS)
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Spacer.Builder()
                    .setWidth(dp(8f))
                    .build()
            )
            .addContent(
                if (counter.maxCount == 0) {
                    Text.Builder(context, counter.currentCount.toString())
                        .setTypography(Typography.TYPOGRAPHY_DISPLAY3)
                        .setColor(argb(stitchCounterColorPalette.onPrimary.toArgb()))
                        .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
                        .build()
                } else {
                    LayoutElementBuilders.Column.Builder()
                        .addContent(
                            if (extraSmallButtons) {
                                Text.Builder(context, counter.currentCount.toString())
                                    .setTypography(Typography.TYPOGRAPHY_TITLE1)
                                    .setColor(argb(stitchCounterColorPalette.onPrimary.toArgb()))
                                    .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
                                    .build()
                            } else {
                                Text.Builder(context, counter.currentCount.toString())
                                    .setTypography(Typography.TYPOGRAPHY_DISPLAY3)
                                    .setColor(argb(stitchCounterColorPalette.onPrimary.toArgb()))
                                    .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
                                    .build()
                            }
                        ).addContent(
                            Text.Builder(context, "/" + counter.maxCount.toString())
                                .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                                .setColor(argb(stitchCounterColorPalette.secondaryVariant.toArgb()))
                                .setOverflow(LayoutElementBuilders.TEXT_OVERFLOW_MARQUEE)
                                .build()
                        ).build()
                }
            )
            .addContent(
                LayoutElementBuilders.Spacer.Builder()
                    .setWidth(dp(8f))
                    .build()
            )
            .addContent(
                Button.Builder(context, clickablePositive)
                    .setContentDescription("Subtract 1")
                    .setIconContent(ID_IC_ADD)
                    .setSize(
                        if (extraSmallButtons) {
                            BUTTON_EXTRA_SMALL_SIZE
                        } else {
                            BUTTON_SMALL_SIZE
                        }
                    )
                    .build()
            )
            .build()
    ).build()

@PreviewTile
fun counterTileLayoutWithoutProgressPreview(context: Context): TilePreviewData {
    return TilePreviewData({ previewResources() }) { request ->
        CounterTileRenderer(context).renderTimeline(
            CounterTileState(
                project = Project(
                    id = 1,
                    name = "project name",
                ),
                counter = Counter(
                    id = 3,
                    name = "pattern",
                    currentCount = 8888,
                    maxCount = 0,
                )
            ),
            request
        )
    }
}

@PreviewTile
fun counterTileLayoutWithProgressPreview(context: Context): TilePreviewData {
    return TilePreviewData({ previewResources() }) { request ->
        CounterTileRenderer(context).renderTimeline(
            CounterTileState(
                project = Project(
                    id = 1,
                    name = "project name",
                ),
                counter = Counter(
                    id = 3,
                    name = "pattern",
                    currentCount = 8888,
                    maxCount = 1200,
                )
            ),
            request
        )
    }
}
