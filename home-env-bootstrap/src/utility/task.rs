use color_eyre::Result;

pub(crate) trait Task {
    fn name(&self) -> String;
    fn execute(&self) -> Result<()>;
}
