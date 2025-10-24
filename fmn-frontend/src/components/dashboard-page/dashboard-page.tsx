import { Component, h, Prop, State, Watch } from '@stencil/core';
import { MatchResults } from '@stencil/router';

@Component({
  tag: 'dashboard-page',
  styleUrl: 'dashboard-page.css',
  shadow: true,
})
export class DashboardPage {
  @State() userRole: string | null = null;
  @State() currentView: string = 'home';
  @State() userid: number;
  @Prop() match: MatchResults;
  @State() page: string;
  handleNavChange(e: string) {
    
    this.currentView = e;
    console.log(this.currentView);
  }
  decodeToken(token: string) {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded;
    } catch (e) {
      console.error('Invalid token', e);
      return null;
    }
  }

  @Watch('match')
  watchMatch() {
    this.page = this.match && this.match.params.page ? this.match.params.page : 'home';
  }
  async componentWillLoad() {
    const token = localStorage.getItem('token');
    if (token) {
      const decoded = this.decodeToken(token);
      console.log('Decoded JWT:', decoded);
      if (decoded && decoded.role) {
        this.userRole = decoded.role;
      } else {
        console.warn('Role not found in token');
      }
      if (decoded && decoded.userId) {
        console.log('User from dashboard', decoded.userId);
        this.userid = decoded.userId;
      }
    } else {
      console.warn('No token found — redirecting to login...');
      window.location.href = '/login';
    }
    this.page = this.match && this.match.params.page ? this.match.params.page : 'home';
  }

  renderChild() {
    switch (this.page) {
      case 'home':
      case undefined:
        return <property-listing user={this.userid}></property-listing>;
      case 'my-properties':
        return <owner-properties user={this.userid} onChangeNav={(e: CustomEvent) => this.handleNavChange(e.detail)}></owner-properties>;
      case 'add-property':
        return <add-property-form user={this.userid}></add-property-form>;
      case 'profile':
        return <user-profile user={this.userid} role={this.userRole}></user-profile>;
      case 'payments':
        return <payments-page userId={this.userid} role={this.userRole}></payments-page>;
      case 'inquiries':
        return <inquiry-page role={this.userRole}></inquiry-page>;
      case 'favorites':
        return <tenant-favorites></tenant-favorites>;
      case 'myRentals':
        return <my-rentals></my-rentals>;
      default:
        return <p>Page not found</p>;
    }
  }
  render() {
    return (
      <div>
        {this.userRole ? <app-navbar role={this.userRole} activeMenu={this.page}></app-navbar> : <p>Loading...</p>}
        <main>
          {/* <stencil-router>
            <stencil-route-switch scrollTopOffset={0}> */}
          {/* Use relative URLs, without repeating /dashboard */}
          {/* <stencil-route url="/home" component="property-listing" componentProps={{ user: this.userid }} exact={true}></stencil-route>
              <stencil-route url="/my-properties" component="owner-properties" componentProps={{ user: this.userid }} exact={true}></stencil-route>
              <stencil-route url="/add-property" component="add-property-form" componentProps={{ user: this.userid }} exact={true}></stencil-route>
              <stencil-route url="/profile" component="user-profile" componentProps={{ user: this.userid, role: this.userRole }} exact={true}></stencil-route>
              <stencil-route url="/payments" component="payments-page" componentProps={{ user: this.userid, role: this.userRole }} exact={true}></stencil-route>
              <stencil-route url="/inquiries" component="inquiry-page" componentProps={{ role: this.userRole }} exact={true}></stencil-route>
              <stencil-route url="/favorites" component="tenant-favorites" exact={true}></stencil-route>
            </stencil-route-switch>
          </stencil-router> */}
          {this.renderChild()}
        </main>
      </div>
    );
  }
}
