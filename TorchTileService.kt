package com.example.godlytorch

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class TorchTileService : TileService() {
    private val controller by lazy { FlashlightController(this) }

    override fun onClick() {
        val active = qsTile?.state == Tile.STATE_ACTIVE
        val newState = !active
        controller.toggleTorch(newState)
        qsTile?.state = if (newState) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile?.updateTile()
    }
}
