import React from "react";
import "./NetworkDetector.scss";
const NetworkDetector = (Component) => {
  return class classComponent extends React.Component {
    constructor(props) {
      super(props);
      this.state = { isDisconnected: false, message: null };
      this.ntwSectionRef = React.createRef();
    }

    updateNetworkStatus = (status, message) => {
      this.setState({ isDisconnected: status, message }, () => {
        if (this.ntwSectionRef.current) {
          this.ntwSectionRef.current.style.backgroundColor = "red";
          this.ntwSectionRef.current.style.display = "block";
        }
      });
    };
    handleNtwChange = () => {
      const status = navigator.onLine ? "online" : "offline";
      const NETWORK_OFFLINE_MSG =
        "No internet connectivity. Please check your network";
      if (status === "online") {
        const timer = setInterval(() => {
          fetch("//google.com", {
            mode: "no-cors",
          })
            .then(() => {
              this.setState(
                (prevState) => ({ ...prevState, message: "Back online" }),
                () => {
                  if (this.ntwSectionRef.current) {
                    this.ntwSectionRef.current.style.backgroundColor = "green";
                  }

                  return clearInterval(timer);
                }
              );

              setTimeout(() => {
                this.setState({ isDisconnected: false });
                if (this.ntwSectionRef.current) {
                  this.ntwSectionRef.current.style.display = "none";
                }
              }, 3000);
            })
            .catch(() => this.updateNetworkStatus(true, NETWORK_OFFLINE_MSG));
        }, [3000]);
      } else {
        this.updateNetworkStatus(true, NETWORK_OFFLINE_MSG);
      }
    };

    componentDidMount() {
      this.handleNtwChange();
      window.addEventListener("online", this.handleNtwChange);
      window.addEventListener("offline", this.handleNtwChange);
    }

    componentWillUnmount() {
      window.removeEventListener("online", this.handleNtwChange);
      window.removeEventListener("offline", this.handleNtwChange);
    }

    render() {
      return (
        <>
          <div>
            {this.state.message && (
              <p className='network_section' ref={this.ntwSectionRef}>
                {this.state.message}
              </p>
            )}
          </div>

          <Component {...this.props} />
        </>
      );
    }
  };
};
export default NetworkDetector;
