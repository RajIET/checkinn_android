package com.example.checkinn_android.ui.designsystem

import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.components.DSButtonSize
import org.junit.Assert.assertEquals
import org.junit.Test

class DSFoundationTest {

    @Test
    fun `verify DS spacing scale`() {
        assertEquals(2.dp, DS.Spacing.xxxs)
        assertEquals(4.dp, DS.Spacing.xxs)
        assertEquals(8.dp, DS.Spacing.xs)
        assertEquals(12.dp, DS.Spacing.sm)
        assertEquals(16.dp, DS.Spacing.md)
        assertEquals(20.dp, DS.Spacing.lg)
        assertEquals(24.dp, DS.Spacing.xl)
        assertEquals(32.dp, DS.Spacing.xxl)
        assertEquals(40.dp, DS.Spacing.xxxl)
    }

    @Test
    fun `verify DS radius scale`() {
        assertEquals(6.dp, DS.Radius.xs)
        assertEquals(8.dp, DS.Radius.sm)
        assertEquals(12.dp, DS.Radius.md)
        assertEquals(16.dp, DS.Radius.lg)
        assertEquals(24.dp, DS.Radius.xl)
    }

    @Test
    fun `verify DS button size paddings`() {
        assertEquals(DS.Spacing.xs, DSButtonSize.Compact.verticalPadding)
        assertEquals(DS.Spacing.sm, DSButtonSize.Compact.horizontalPadding)
        assertEquals(DS.Spacing.sm, DSButtonSize.Regular.verticalPadding)
        assertEquals(DS.Spacing.md, DSButtonSize.Regular.horizontalPadding)
    }
}
