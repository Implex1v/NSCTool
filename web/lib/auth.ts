/**
 * Types of auth required by a page.
 */
export enum AuthOption {
    Required,
    Optional,
    None
}

/**
 * Roles assigned to user by IdP.
 */
export enum Roles {
    User = "api-user",
    Admin = "api-admin"
}

/**
 * Type exported by a component to configure authentication for a page.
 */
export type PageAuth = {
    auth?: AuthOption,
    role?: Roles
}
