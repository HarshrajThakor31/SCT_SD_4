# 📖 Temperature Converter Pro - User Guide

## Table of Contents
1. [Getting Started](#getting-started)
2. [Basic Operations](#basic-operations)
3. [Advanced Features](#advanced-features)
4. [Keyboard Shortcuts](#keyboard-shortcuts)
5. [Troubleshooting](#troubleshooting)

## Getting Started

### System Requirements
- Java 8 or higher
- Windows, macOS, or Linux
- Minimum 512MB RAM
- 50MB free disk space

### Installation
1. Download the latest release from GitHub
2. Extract the files to your desired location
3. Open terminal/command prompt in the project directory
4. Run: `java -cp src TemperatureConverter`

## Basic Operations

### Converting Temperatures

1. **Enter Temperature**
   - Type a number in the "Temperature" field
   - Or use the slider below for visual input
   - The application accepts positive and negative numbers

2. **Select Scales**
   - Choose input scale from "From" dropdown
   - Choose output scale from "To" dropdown
   - Available scales: Celsius, Fahrenheit, Kelvin, Rankine, Réaumur

3. **View Results**
   - Results appear instantly as you type (real-time mode)
   - Click "Convert" to add to history
   - All scales are shown simultaneously in the results panel

### Understanding the Interface

#### Left Panel - Input Controls
- **Temperature Field**: Direct number entry
- **Slider**: Visual temperature selection (-50°C to 150°C)
- **From/To Dropdowns**: Scale selection with swap button (⇄)
- **Precision Spinner**: Decimal places control (0-6)
- **Sound Effects**: Toggle audio feedback
- **Action Buttons**: Convert, Clear, Copy, Favorite, Random, Theme

#### Center Panel - Results Display
- **Main Result**: Primary conversion with validation warnings
- **All Scales Display**: Shows temperature in all 5 scales
- **Visual Thermometer**: Animated thermometer with color coding
  - Blue: Below 0°C (freezing)
  - Green: 0-30°C (cool to room temperature)
  - Orange: 30-60°C (warm)
  - Red: Above 60°C (hot)

#### Right Panel - Information Tabs
- **History**: Complete log of all conversions with timestamps
- **Favorites**: Saved frequently used conversions
- **Quick Reference**: Common temperature conversion values

## Advanced Features

### Real-Time Conversion
- Conversions update automatically as you type
- No need to click "Convert" for immediate results
- Useful for exploring temperature relationships

### Temperature Validation
- ⚠️ Warning appears for temperatures below absolute zero
- Absolute zero limits:
  - Celsius: -273.15°C
  - Fahrenheit: -459.67°F
  - Kelvin: 0K
  - Rankine: 0°R
  - Réaumur: -218.52°Ré

### Precision Control
- Adjust decimal places from 0 to 6
- Higher precision useful for scientific calculations
- Lower precision better for everyday use

### History Management
- All conversions automatically saved to history
- Timestamps show when conversion was performed
- Scroll through previous conversions
- History persists during session

### Favorites System
- Click "★ Favorite" to save current conversion
- Access saved conversions in Favorites tab
- Useful for frequently needed temperature values
- Examples: Body temperature, cooking temperatures, weather

### Visual Thermometer
- Real-time visual representation of temperature
- Color-coded for easy temperature range identification
- Scale markings for reference
- Current temperature displayed at top

### Theme Support
- Toggle between Light and Dark themes
- Click "Theme" button to switch
- Preference applies to entire interface
- Improves usability in different lighting conditions

## Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Enter` | Perform conversion and add to history |
| `Escape` | Clear all fields and reset |
| `Ctrl+R` | Generate random temperature |
| `Ctrl+C` | Copy result to clipboard |
| `Tab` | Navigate between input fields |
| `Space` | Toggle checkboxes and buttons |

## Troubleshooting

### Common Issues

**Problem**: "Invalid number format" error
- **Solution**: Ensure you're entering only numbers (including decimals)
- **Valid**: 25, -10, 98.6, 0
- **Invalid**: 25°, twenty-five, 25C

**Problem**: Application won't start
- **Solution**: Check Java installation with `java -version`
- **Requirement**: Java 8 or higher needed

**Problem**: Slider not updating input field
- **Solution**: Click and drag slider, then release
- **Note**: Updates occur when slider movement stops

**Problem**: Results seem incorrect
- **Solution**: Verify input scale selection
- **Check**: Ensure you're reading the correct output scale
- **Reference**: Use Quick Reference tab for common conversions

### Performance Tips

1. **For Large Numbers**: Use scientific notation (1.5E+3 for 1500)
2. **For Precision**: Adjust decimal places as needed
3. **For Speed**: Use real-time mode instead of clicking Convert
4. **For Accuracy**: Double-check scale selections

### Getting Help

- Check the Quick Reference tab for common conversions
- Verify your input format and scale selections
- Ensure Java is properly installed and updated
- For technical issues, refer to the project's GitHub repository

## Temperature Scale Information

### Celsius (°C)
- Water freezes at 0°C, boils at 100°C
- Most common scale worldwide
- Used in science and daily life

### Fahrenheit (°F)
- Water freezes at 32°F, boils at 212°F
- Primarily used in United States
- Named after Daniel Gabriel Fahrenheit

### Kelvin (K)
- Absolute temperature scale
- 0K is absolute zero (-273.15°C)
- Used in scientific calculations

### Rankine (°R)
- Absolute scale based on Fahrenheit
- 0°R is absolute zero (-459.67°F)
- Used in engineering applications

### Réaumur (°Ré)
- Historical temperature scale
- Water freezes at 0°Ré, boils at 80°Ré
- Rarely used today, mainly historical interest

---

*For additional support or feature requests, please visit the project's GitHub repository.*