package com.squad.view.join;

class JoinSquadPresenter {

    private JoinSquadStateMapper stateMapper;
    private JoinSquadModel model;

    JoinSquadPresenter(JoinSquadView view) {
        stateMapper = new JoinSquadStateMapper(view);
        model = new JoinSquadModel();

        init();
    }

    private void init() {
        model.getLobbies().subscribe(lobbies -> stateMapper.renderLobbiesReceivedState(lobbies));
    }
}
