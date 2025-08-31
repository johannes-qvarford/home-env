use color_eyre::{eyre::Context, Result};

use crate::{
    utility::process,
    utility::task::{self},
};

pub(crate) struct PortForwardingTask;

impl task::Task for PortForwardingTask {
    fn name(&self) -> String {
        format!("port_forwarding_task")
    }

    fn execute(&self) -> Result<()> {
        let proxy_command = r#"netsh interface portproxy add v4tov4 listenport=64342 listenaddress=0.0.0.0 connectport=64342 connectaddress=127.0.0.1"#;
        let firewall_command = r#"netsh advfirewall firewall add rule name="WSL MCP Proxy" dir=in action=allow protocol=TCP localport=64342"#;
        let acceptable_status_codes = [0];

        process::execute("cmd.exe", &["/c", proxy_command], &acceptable_status_codes)
            .wrap_err_with(|| format!("Executing port proxy command: '{proxy_command}'"))?;

        process::execute(
            "cmd.exe",
            &["/c", firewall_command],
            &acceptable_status_codes,
        )
        .wrap_err_with(|| format!("Executing firewall rule command: '{firewall_command}'"))
    }
}

pub(crate) fn port_forwarding_task() -> Box<dyn task::Task> {
    Box::new(PortForwardingTask)
}
