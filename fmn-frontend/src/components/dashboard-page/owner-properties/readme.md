# owner-properties



<!-- Auto Generated Below -->


## Properties

| Property | Attribute | Description | Type     | Default     |
| -------- | --------- | ----------- | -------- | ----------- |
| `user`   | `user`    |             | `number` | `undefined` |


## Dependencies

### Used by

 - [dashboard-page](..)

### Depends on

- [property-listing](../property-listing)
- [add-property-form](../add-property-form)

### Graph
```mermaid
graph TD;
  owner-properties --> property-listing
  owner-properties --> add-property-form
  property-listing --> property-card
  dashboard-page --> owner-properties
  style owner-properties fill:#f9f,stroke:#333,stroke-width:4px
```

----------------------------------------------

*Built with [StencilJS](https://stenciljs.com/)*
