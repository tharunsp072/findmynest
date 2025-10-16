import { Component, h, Prop, State, Event, EventEmitter, Method } from '@stencil/core';

@Component({
  tag: 'dashboard-page',
  styleUrl: 'dashboard-page.css',
  shadow: true,
})
export class DashboardPage {
  @State() userRole: string | null = null;
  @State() currentView: string = 'home';
  @State() userid:number;

  handleNavChange(e: CustomEvent<string>) {
    this.currentView = e.detail;
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
      if (decoded && decoded.userId){
        console.log("User from dashboard",decoded.userId);
        this.userid = decoded.userId;
      }
     
    } else {
      console.warn('No token found — redirecting to login...');
      window.location.href = '/login';
    }
  }


  render() {
    return (
      <div>
        {this.userRole ? (
          <app-navbar
            role={this.userRole}
            onHandleNav={(e: CustomEvent<string>) => {
              this.handleNavChange(e);
            }}
          ></app-navbar>
        ) : (
          <p>Loading...</p>
        )}
        <main>
          {this.currentView === 'home' && <property-listing user={this.userid}></property-listing>}
          {this.currentView === 'my-properties' && this.userid != null && <owner-properties user={this.userid}></owner-properties>}
          {this.currentView === 'add-property' && this.userid != null && (
            <add-property-form user={this.userid} onPageRender={(e: CustomEvent<string>) => this.handleNavChange(e)}></add-property-form>
          )}
          {this.currentView === 'favorites' && <tenant-favorites></tenant-favorites>}
          {this.currentView === 'profile' && this.userid != null && <user-profile user={this.userid} role={this.userRole}></user-profile>}
          {(this.currentView === 'owner-payments' || this.currentView === 'payments') && <payments-page role={this.userRole} userId={this.userid}></payments-page>}
          {(this.currentView === 'owner-inquires' || this.currentView === 'inquiries') && <inquiry-page role={this.userRole}></inquiry-page>}
        </main>
      </div>
    );
  }
}
