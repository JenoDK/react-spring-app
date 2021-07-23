import React, { Component } from "react";
import { Button, ButtonProps, Spinner } from "react-bootstrap";
import "./LoaderButton.scss";

export interface LoaderButtonProps {
    isLoading: boolean;
    text: string;
    loadingText: string;
    className?: string;
    disabled?: boolean;
    buttonProps?: ButtonProps;
}

export default class LoaderButton extends Component<LoaderButtonProps, {}> {
    public static defaultProps: Partial<LoaderButtonProps> = {
        buttonProps: {
            block: true,
            type: "submit",
        }
    };
    render() {
        var { className, disabled, isLoading, text, loadingText, buttonProps } = this.props;
        return (
            <Button
                className={`LoaderButton ${className}`}
                disabled={disabled || isLoading}
                {...buttonProps}
            >
                {isLoading &&
                    <Spinner
                        as="span"
                        animation="border"
                        size="sm"
                        role="status"
                        aria-hidden="true"
                        className="spinner"
                    />}
                {!isLoading ? text : loadingText}
            </Button>
        );
    }
}

// export default ({ isLoading, className = "", disabled = false, text, loadingText, ...rest }: LoaderButtonProps) => (
//     <Button
//         className={`LoaderButton ${className}`}
//         disabled={disabled || isLoading}
//         {...rest}
//     >
//         {isLoading &&
//             <Spinner
//                 as="span"
//                 animation="border"
//                 size="sm"
//                 role="status"
//                 aria-hidden="true"
//                 className="spinner"
//             />}
//         {!isLoading ? text : loadingText}
//     </Button>
// );