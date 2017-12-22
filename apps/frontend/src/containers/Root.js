import React from 'react'
import {Toolbar, ToolbarRow, ToolbarTitle} from 'rmwc/Toolbar/index';
import {Tab, TabBar} from 'rmwc/Tabs/index';
import {
    Card, CardAction, CardActions, CardMedia, CardPrimary, CardSubtitle, CardSupportingText,
    CardTitle
} from 'rmwc/Card/index';
import {Grid, GridCell} from 'rmwc/Grid/index';

const Root = () => (
    <div>
        <Toolbar>
            <ToolbarRow>
                <ToolbarTitle>Toolbar</ToolbarTitle>
            </ToolbarRow>
            <ToolbarRow>
                <TabBar>
                    {/*activeTabIndex={this.state.activeTabIndex || 0}*/}
                    {/*onChange={evt => this.setState({activeTabIndex: evt.target.value})}*/}
                    <Tab>Cookies</Tab>
                    <Tab>Pizza</Tab>
                    <Tab>Icecream</Tab>
                </TabBar>
            </ToolbarRow>
        </Toolbar>

        <Grid>
            <GridCell span="2"/>
            <GridCell span="8">
                <Card>
                    <CardMedia
                        style={{
                            backgroundImage: 'url(https://material-components-web.appspot.com/images/16-9.jpg)',
                            height: '12.313rem'
                        }}
                    />
                    <CardPrimary>
                        <CardTitle large>Card Title</CardTitle>
                        <CardSubtitle>Subtitle here</CardSubtitle>
                    </CardPrimary>
                    <CardSupportingText/>
                    <CardActions>
                        <CardAction>Action 1</CardAction>
                        <CardAction>Action 2</CardAction>
                    </CardActions>
                </Card>
            </GridCell>
            <GridCell span="2"/>
        </Grid>
    </div>
);

export default Root
