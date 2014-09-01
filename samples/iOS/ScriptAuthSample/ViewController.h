//
//  ViewController.h
//  FF-IOS-ScriptAuthTest
//
//  Created by David Wells on 2/17/13.
//  Copyright (c) 2013 FatFractal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WebViewController.h"

@interface ViewController : UIViewController <WebViewControllerDelegate>

@property (weak, nonatomic) IBOutlet UIView *loggedOutView;
@property (weak, nonatomic) IBOutlet UIButton *facebookButton;
@property (weak, nonatomic) IBOutlet UIButton *twitterButton;
@property (weak, nonatomic) IBOutlet UILabel *errorLabel;

@property (weak, nonatomic) IBOutlet UIView *loggedInView;
@property (weak, nonatomic) IBOutlet UILabel *userNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *serviceLabel;
@property (weak, nonatomic) IBOutlet UIButton *logOutButton;
- (IBAction)logOut:(id)sender;

@end
