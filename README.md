# Book finder

A lightweight Spigot/Paper Minecraft server plugin that helps players locate specific enchanted books stored in nearby chests, barrels, and shulker boxes.

## Features

*   **Proximity Search:** Scans all containers (Chests, Barrels, Shulker Boxes) in the player's current chunk and the 8 surrounding chunks (3x3 chunk area).
*   **Smart Filtering:** Search for books by enchantment name (e.g., "Mending", "Sharpness"). Supports partial matching (minimum 3 characters).
*   **Visual Assistance:**
    *   Automatically rotates your camera to face the closest container with the matching book.
    *   Provides clickable chat messages to easily "look at" other found containers.
*   **Detailed Info:** Tells you exactly where the book is located inside the chest (Row and Column number).
*   **Bilingual Support:** Searches enchantment names in both English and German based on your client locale (if configured).

## Installation

1.  Download the latest release jar.
2.  Place the file into your server's `plugins` folder.
3.  Restart your server.

## Commands

### `/findbook <enchantment>`
Searches for an enchanted book with the specified name.

**Example:**
`/findbook mend`
*   *Searches for "Mending" in nearby containers.*

**Example:**
`/findbook sharp`
*   *Searches for "Sharpness" in nearby containers.*

## Compatibility

*   **Minecraft Version:** 1.21+
*   **Server Software:** Spigot, Paper, Purpur, etc.

## How it works

1.  Stand in the area where your storage is.
2.  Type `/findbook <name>`.
3.  The plugin will check every chest around you.
4.  If a match is found, your camera will turn to face the chest, and you will receive a chat message with details.