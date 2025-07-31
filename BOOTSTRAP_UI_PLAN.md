# Bootstrap UI Implementation Plan

## Overview
Transform the current CLI-based bootstrap tool into a GUI application while maintaining existing functionality and architecture.

## Core Requirements Analysis
- **Current State**: CLI tool with sequential task execution, mark files for tracking, stdout output
- **Target State**: GUI with task selection, visual progress indicators, log panel, file logging
- **Key Challenges**: TTY handling for user prompts, maintaining cross-platform compatibility

## Implementation Strategy

### Phase 1: Architecture Foundation
1. **GUI Framework Selection**: Use `egui` (immediate mode GUI)
   - Cross-platform (Windows/Linux)
   - Minimal dependencies 
   - Good terminal/process integration support
   - Easy integration with existing Rust codebase

2. **Logging System Abstraction**
   - Create `LogManager` trait/struct to handle dual output (file + GUI buffer)
   - Swedish timestamp format: `YYYY-MM-DD HH:mm:ss`
   - Strip ANSI colors for file output, preserve for GUI panel
   - Use standard config directory for log files

3. **Task Status Management Abstraction**
   - Extend existing mark file system with abstraction layer
   - Prepare for future TOML migration while maintaining current behavior
   - Enhanced status tracking (success/failure/in-progress)

### Phase 2: Core GUI Implementation
1. **Main Window Layout**
   - Task list with checkmarks (✅ executed, ❌ not executed) 
   - "Run All Remaining" and "Run Selected Task" buttons
   - Real-time log output panel with scrolling
   - User prompt indicator/field

2. **Task Execution Integration**
   - Background task execution with progress updates
   - Non-blocking GUI updates during task execution
   - Graceful handling of task failures

### Phase 3: Advanced Features
1. **TTY/Process Handling**
   - Implement pseudo-TTY for interactive commands
   - Handle user prompts within GUI context
   - Maintain stdin/stdout/stderr redirection to GUI

2. **Enhanced User Experience**
   - Task filtering and search
   - Execution history
   - Progress indicators
   - Color-coded log output

### Phase 4: Testing & Polish
1. **Cross-platform testing** (Windows/Linux)
2. **Integration with existing workflows**
3. **Performance optimization**
4. **Documentation updates**

## Technical Implementation Details

### New Dependencies
```toml
egui = "0.24"
eframe = { version = "0.24", default-features = false, features = ["default_fonts", "glow"] }
chrono = { version = "0.4", features = ["serde"] }
tokio = { version = "1.0", features = ["full"] }
```

### Key Components
- `gui/mod.rs` - Main GUI application logic
- `gui/app.rs` - Application state and UI rendering  
- `gui/task_panel.rs` - Task list and controls
- `gui/log_panel.rs` - Output display and logging
- `utility/logging.rs` - Logging abstraction
- `utility/task_status.rs` - Enhanced task status management

### File Structure Changes
```
src/
├── gui/           # New GUI components
│   ├── mod.rs
│   ├── app.rs
│   ├── task_panel.rs
│   └── log_panel.rs
├── utility/
│   ├── logging.rs    # New logging abstraction
│   └── task_status.rs # Enhanced task management
└── main.rs        # Updated with GUI/CLI mode selection
```

## Migration Strategy
- Maintain CLI mode as fallback option (`--cli` flag)
- Preserve all existing functionality
- Backward compatible mark file system
- Gradual rollout with feature flags

## Success Criteria
- ✅ GUI displays all tasks with correct execution status
- ✅ Can run individual tasks or all remaining tasks
- ✅ Real-time log output with timestamps
- ✅ User prompt handling works correctly
- ✅ Cross-platform compatibility maintained
- ✅ Log files created in standard config directory
- ✅ No regression in existing CLI functionality

## Final Step
Delete `BOOTSTRAP_UI_PLAN.md` once user confirms everything works correctly.