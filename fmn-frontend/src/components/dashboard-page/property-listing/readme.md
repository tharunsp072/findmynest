# property-listing



<!-- Auto Generated Below -->


## Properties

| Property            | Attribute | Description | Type         | Default     |
| ------------------- | --------- | ----------- | ------------ | ----------- |
| `listingProperties` | --        |             | `Property[]` | `[]`        |
| `source`            | `source`  |             | `string`     | `undefined` |
| `user`              | `user`    |             | `number`     | `undefined` |


## Dependencies

### Used by

 - [dashboard-page](..)
 - [owner-properties](../owner-properties)

### Depends on

- [property-card](property-card)

### Graph
```mermaid
graph TD;
  property-listing --> property-card
  property-card --> inquiry-form
  dashboard-page --> property-listing
  owner-properties --> property-listing
  style property-listing fill:#f9f,stroke:#333,stroke-width:4px
```

----------------------------------------------

*Built with [StencilJS](https://stenciljs.com/)*
