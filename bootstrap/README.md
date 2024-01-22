Commands to cross-compile to windows from Ubuntu.

```
rustup target add x86_64-pc-windows-gnu
sudo apt-get install -y mingw-w64
cargo run --target x86_64-pc-windows-gnu
```