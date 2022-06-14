import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export function Input({label, setValue, type = InputType.Text, required = false, icon = null}) {
    return (
        <div>
            <label htmlFor={label} className="form-label">{label}</label>
            <div className="input-group">
                {icon ?
                    <span className="input-group-text">
                        <FontAwesomeIcon icon={icon} />
                    </span>
                    :
                    ""}
                <input type={type} onChange={e => setValue(e.target.value)} id={label} className="form-control" required={required} />
            </div>
        </div>
    )
}

export function InputText({label, setValue, required = false, icon = null}) {
    return (
        <Input required={required} type={InputType.Text} setValue={setValue} label={label} icon={icon} />
    )
}

export function InputEmail({label, setValue, required = false, icon = null}) {
    return (
        <Input required={required} type={InputType.Email} setValue={setValue} label={label} icon={icon} />
    )
}

export function InputPassword({label, setValue, required = false, icon = null}) {
    return (
        <Input required={required} type={InputType.Password} setValue={setValue} label={label} icon={icon} />
    )
}

export enum InputType {
    Text = "text",
    Email = "email",
    Password = "password"
}
