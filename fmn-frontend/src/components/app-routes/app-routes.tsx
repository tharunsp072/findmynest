import { Component, h} from '@stencil/core';
import '@stencil/router';
@Component({
  tag: 'app-routes',
  styleUrl: 'app-routes.css',
  shadow: true,
})
export class AppRoutes {
  render() {
    return (
      <div>
        <stencil-router>
          <stencil-router-switch scrollTopOffset={0}>
            <stencil-route url="/" component="login-page" exact={true}></stencil-route>
            <stencil-route url="/dashboard" component="dashboard-page" exact={true}></stencil-route>
            <stencil-route url="/addProperty" component="add-property-form"></stencil-route>
          </stencil-router-switch>
        </stencil-router>
      </div>
    );
  }
}
