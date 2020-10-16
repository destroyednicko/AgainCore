package com.nicko.core.profile.staff;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileStaffOptions {

    private boolean staffModeEnabled = false;
    private boolean staffChatModeEnabled = false;
    private boolean receiveStaffChat = true;

    private boolean socialSpy = false;
    private boolean vanish = false;
    private boolean hideStaff = false;

}
