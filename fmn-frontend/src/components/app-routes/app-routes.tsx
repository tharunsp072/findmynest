import { Component, h, State } from '@stencil/core';
import '@stencil/router';
@Component({
  tag: 'app-routes',
  styleUrl: 'app-routes.css',
  // shadow: true,
})
export class AppRoutes {
  @State() token: string | null = null;
  componentWillLoad() {
    this.token = localStorage.getItem('token');
  }
  render() {
    return (
      <stencil-router>
        <stencil-router-switch scrollTopOffset={0}>
          <stencil-route url="/" component="redirect-home" exact={true}></stencil-route>
          <stencil-route url="/login" component="login-page" exact={true}></stencil-route>
          <stencil-route url="/dashboard/:page?" component="dashboard-page" exact={false}></stencil-route>{' '}
          {/* <stencil-route url="/addProperty" component="add-property-form"></stencil-route> */}
        </stencil-router-switch>
      </stencil-router>
    );
  }
}
